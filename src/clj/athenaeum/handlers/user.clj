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

(defn- verify-id-token
  "Returns payload map"
  [id-token-string]
  (let [verifier (.build (.setAudience (GoogleIdTokenVerifier$Builder. (GoogleNetHttpTransport/newTrustedTransport)
                                                                       (JacksonFactory.))
                                       [(get-in @config/config [:oauth :google :client-id])]))]
    (when-let [id-token (try (.verify ^GoogleIdTokenVerifier verifier ^String id-token-string)
                             (catch Exception _ nil))]
      (walk/keywordize-keys (into {} (.getPayload id-token))))))

(defn find-or-create-user
  "Returns user id"
  [payload]
  (if-let [user (db/with-transaction [tx @db/datasource]
                  (user/fetch-by-google-id tx (:sub payload)))]
    (:id user)
    (:id (db/with-transaction [tx @db/datasource]
           (user/create tx payload)))))

(defn- create-session
  "Returns session id"
  [user-id]
  (session/create-and-return-id user-id))

(defn- set-session-id-cookie
  [response session-id]
  (response/set-cookie response
                       "session-id"
                       session-id
                       {:same-site :strict
                        :max-age   3600}))

(defn login
  [{:keys [headers]}]
  (if-let [id-token (:id-token (walk/keywordize-keys headers))]
    (if-let [payload (verify-id-token id-token)]
      (set-session-id-cookie (response/response {:message "login success"})
                             (create-session (find-or-create-user payload)))
      (response/bad-request {:message "login failed"}))
    (response/bad-request {:message "id token header missing"})))

(defn logout
  [{:keys [cookies]}]
  (if-let [session-id (get-in cookies ["session-id" :value])]
    (if (session/exists? session-id)
      (do (session/delete session-id)
          (response/response {:message "session deleted"}))
      (response/bad-request {:message "session doesn't exist"}))
    (response/bad-request {:message "session id cookie missing"})))
