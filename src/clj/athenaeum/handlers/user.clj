(ns athenaeum.handlers.user
  (:require [ring.util.response :as response]
            [athenaeum.domain.user :as user]
            [athenaeum.db :as db]
            [athenaeum.config :as config]
            [clojure.walk :as walk])
  (:import (com.google.api.client.googleapis.auth.oauth2 GoogleIdTokenVerifier GoogleIdTokenVerifier$Builder)
           (com.google.api.client.googleapis.javanet GoogleNetHttpTransport)
           (com.google.api.client.json.jackson2 JacksonFactory)))

(defn verify-id-token
  [id-token-string]
  (let [verifier (.build (.setAudience (GoogleIdTokenVerifier$Builder. (GoogleNetHttpTransport/newTrustedTransport)
                                                                       (JacksonFactory.))
                                       [(get-in @config/config [:oauth :google :client-id])]))]
    (when-let [id-token (.verify ^GoogleIdTokenVerifier verifier ^String id-token-string)]
      (walk/keywordize-keys (into {} (.getPayload id-token))))))

(defn confirm-login
  [payload]
  (if (db/with-transaction [tx @db/datasource]
        (user/fetch-one tx (:sub payload)))
    1
    (db/with-transaction [tx @db/datasource]
      (user/create tx payload))))

(defn login
  [{:keys [headers]}]
  (let [id-token (:id-token (walk/keywordize-keys headers))]
    (if (verify-id-token id-token)
      (response/response "login success")
      (response/bad-request "login failure"))))
