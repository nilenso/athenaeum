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
  [id-token-string]
  (let [client-id (get-in @config/config [:oauth :google :client-id])
        id-token-verifier (-> (GoogleNetHttpTransport/newTrustedTransport)
                              (GoogleIdTokenVerifier$Builder. (JacksonFactory.))
                              (.setAudience [client-id])
                              (.build))]
    (try (.verify ^GoogleIdTokenVerifier id-token-verifier ^String id-token-string)
         (catch Exception _ nil))))

(defn- get-payload
  [id-token]
  (->> id-token
       (.getPayload)
       (into {})
       (walk/keywordize-keys)
       (reformat-payload)))

(defn- find-or-create-user
  [payload]
  (if-let [user (db/with-transaction [tx @db/datasource]
                  (user/fetch-by-google-id tx (:google-id payload)))]
    user
    (db/with-transaction [tx @db/datasource]
      (user/create tx payload))))

(defn- set-session-id-cookie
  [response session-id]
  (response/set-cookie response
                       :session-id
                       session-id
                       {:same-site :strict
                        :max-age   3600
                        :path      "/api"}))

(defn login
  [{:keys [headers]}]
  (if-let [payload (some-> headers
                           :id-token
                           verify-id-token
                           get-payload)]
    (if (= (:domain payload) "nilenso.com")
      (-> (response/response {:message "login success"})
          (set-session-id-cookie (-> payload
                                     find-or-create-user
                                     :id
                                     session/create)))
      (-> (response/response {:message "invalid domain"})
          (response/status 401)))
    (response/bad-request {:message "id token verification failed"})))

(defn logout
  [{:keys [cookies]}]
  (let [session-id (get-in cookies [:session-id :value])]
    (session/delete session-id)
    (response/response {:message "session deleted"})))

(defn user
  [{:keys [cookies]}]
  (let [session-id (get-in cookies [:session-id :value])
        user-id (session/fetch session-id)
        user (db/with-transaction [tx @db/datasource]
               (user/fetch-by-id tx user-id))]
    (response/response {:user user})))
