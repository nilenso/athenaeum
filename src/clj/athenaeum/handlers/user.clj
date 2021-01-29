(ns athenaeum.handlers.user
  (:require [ring.util.response :as response]
            [clojure.walk :as walk]
            [clojure.string :as str]
            [athenaeum.config :as config]
            [athenaeum.db :as db]
            [athenaeum.session :as session]
            [athenaeum.domain.user :as user])
  (:import (com.google.api.client.googleapis.auth.oauth2 GoogleIdTokenVerifier GoogleIdTokenVerifier$Builder)
           (com.google.api.client.googleapis.javanet GoogleNetHttpTransport)
           (com.google.api.client.json.jackson2 JacksonFactory)))

(defonce id-token-verifier (atom nil))

(defn set-id-token-verifier
  []
  (reset! id-token-verifier (-> (GoogleNetHttpTransport/newTrustedTransport)
                                (GoogleIdTokenVerifier$Builder. (JacksonFactory.))
                                (.setAudience [(get-in @config/config [:oauth :google :client-id])])
                                (.build))))

(defn reset-id-token-verifier
  [new-value]
  (reset! id-token-verifier new-value))

(defn- reformat-payload
  [payload]
  {:google-id (:sub payload)
   :email     (:email payload)
   :name      (:name payload)
   :domain    (:hd payload)
   :photo-url (:picture payload)})

(defn- verify-id-token
  [id-token-string]
  (try (.verify ^GoogleIdTokenVerifier @id-token-verifier ^String id-token-string)
       (catch Exception _ nil)))

(defn- get-payload
  [id-token]
  (->> id-token
       (.getPayload)
       (into {})
       (walk/keywordize-keys)
       (reformat-payload)))

(defn- set-session-id-cookie
  [response session-id]
  (response/set-cookie response
                       :session-id
                       session-id
                       {:same-site :strict
                        :max-age   (get @config/config :session-ttl)
                        :path      "/api"}))

(defn login
  [{:keys [headers]}]
  (if-let [id-token (some-> headers
                            walk/keywordize-keys
                            :authorization
                            (str/split #" ")
                            last)]
    (if-let [user (some-> id-token
                          verify-id-token
                          get-payload)]
      (if (= (:domain user) "nilenso.com")
        (-> (response/response {:message "login success"})
            (set-session-id-cookie (-> (db/with-transaction [tx @db/datasource]
                                         (user/find-by-google-id-or-create tx user))
                                       :id
                                       session/create)))
        (response/bad-request {:message "invalid domain"}))
      (response/bad-request {:message "id token verification failed"}))
    (response/bad-request {:message "id token header missing"})))

(defn logout
  [{:keys [cookies]}]
  (if-let [session-id (get-in (walk/keywordize-keys cookies) [:session-id :value])]
    (do (session/delete session-id)
        (response/response {:message "session deleted"}))
    (response/bad-request {:message "session id cookie missing"})))

(defn user
  [{:keys [cookies]}]
  (let [session-id (get-in cookies [:session-id :value])
        user-id (session/fetch session-id)
        user (db/with-transaction [tx @db/datasource]
               (user/find-by-id tx user-id))]
    (response/response {:user user})))
