(ns athenaeum.handlers.user
  (:require [ring.util.response :as response]
            [athenaeum.domain.user :as user]
            [athenaeum.db :as db]
            [athenaeum.config :as config]
            [clojure.walk :as walk])
  (:import (com.google.api.client.googleapis.auth.oauth2 GoogleIdTokenVerifier GoogleIdTokenVerifier$Builder)
           (com.google.api.client.googleapis.javanet GoogleNetHttpTransport)
           (com.google.api.client.json.jackson2 JacksonFactory)))

(defn- verify-id-token
  [id-token-string]
  (let [verifier (.build (.setAudience (GoogleIdTokenVerifier$Builder. (GoogleNetHttpTransport/newTrustedTransport)
                                                                       (JacksonFactory.))
                                       [(get-in @config/config [:oauth :google :client-id])]))]
    (when-let [id-token (try (.verify ^GoogleIdTokenVerifier verifier ^String id-token-string)
                             (catch Exception _ nil))]
      (walk/keywordize-keys (into {} (.getPayload id-token))))))

(defn- create-session
  []
  1)

(defn- confirm-login
  [payload]
  (when-not (db/with-transaction [tx @db/datasource]
              (user/fetch-by-google-id tx (:sub payload)))
    (db/with-transaction [tx @db/datasource]
      (user/create tx payload)))
  (create-session))

(defn login
  [{:keys [headers]}]
  (if-let [id-token (:id-token (walk/keywordize-keys headers))]
    (if-let [payload (verify-id-token id-token)]
      (do (confirm-login payload)
          (response/response {:text "login success"}))
      (response/bad-request {:text "login failed"}))
    (response/bad-request {:text "Id token header missing"})))
