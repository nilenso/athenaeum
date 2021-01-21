(ns athenaeum.handlers.user
  (:require [ring.util.response :as response]
            [clojure.walk :as walk]
            [athenaeum.config :as config]
            [athenaeum.db :as db]
            [athenaeum.session :as session]
            [athenaeum.domain.user :as user])
  (:import (com.google.api.client.googleapis.auth.oauth2 GoogleIdTokenVerifier GoogleIdTokenVerifier$Builder)
           (com.google.api.client.googleapis.javanet GoogleNetHttpTransport)
           (com.google.api.client.json.jackson2 JacksonFactory)))

(defn- reformat-payload
  [payload]
  {:google-id (:sub payload)
   :email     (:email payload)
   :name      (:name payload)
   :domain    (:hd payload)
   :photo-url (:picture payload)})

(defn- verify-id-token
  "Returns payload map"
  [id-token-string]
  (let [verifier (.build (.setAudience (GoogleIdTokenVerifier$Builder. (GoogleNetHttpTransport/newTrustedTransport)
                                                                       (JacksonFactory.))
                                       [(get-in @config/config [:oauth :google :client-id])]))]
    (when-let [id-token (try (.verify ^GoogleIdTokenVerifier verifier
                                      ^String id-token-string)
                             (catch Exception _ nil))]
      (->> id-token
           (.getPayload)
           (into {})
           (walk/keywordize-keys)
           (reformat-payload)))))

(defn find-or-create-user
  "Returns user id"
  [payload]
  (if-let [user (db/with-transaction [tx @db/datasource]
                  (user/fetch-by-google-id tx (:google-id payload)))]
    (:id user)
    (:id (db/with-transaction [tx @db/datasource]
           (user/create tx payload)))))

(defn- create-session
  "Returns session id"
  [user-id]
  (session/create user-id))

(defn- set-session-id-cookie
  [response session-id]
  (response/set-cookie response
                       :session-id
                       session-id
                       {:same-site :strict
                        :max-age   3600
                        :path "/api"}))

(defn login
  [{:keys [headers]}]
  (let [id-token (:id-token headers)]
    (if-let [payload (verify-id-token id-token)]
      (if (= (:domain payload) "nilenso.com")
        (set-session-id-cookie (response/response {:message "login success"})
                               (create-session (find-or-create-user payload)))
        (-> (response/response {:message "invalid domain"})
            (response/status 401)))
      (response/bad-request {:message "id token verification failed"}))))

(defn logout
  [{:keys [cookies]}]
  (let [session-id (get-in cookies [:session-id :value])]
    (if (session/exists? session-id)
      (do (session/delete session-id)
          (response/response {:message "session deleted"}))
      (response/bad-request {:message "session doesn't exist"}))))

(defn session
  [{:keys [cookies]}]
  (let [session-id (get-in cookies [:session-id :value])]
    (if (session/exists? session-id)
      (response/response {:session true})
      (-> (response/response {:session false})
          (response/status 401)))))
