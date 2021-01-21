(ns athenaeum.session
  (:require [athenaeum.redis :as redis]
            [athenaeum.config :as config])
  (:import (java.util UUID)))

(defn- new-id
  []
  (str (UUID/randomUUID)))

(defn create
  "returns id of created session"
  [user-id]
  (let [session user-id
        session-id (new-id)]
    (redis/set-key session-id session (:session-ttl @config/config))
    session-id))

(defn exists?
  [session-id]
  (redis/key-exists? session-id))

(defn fetch
  [session-id]
  (when-let [user-id (redis/get-value session-id)]
    (Integer/parseInt user-id)))

(defn delete
  [session-id]
  (redis/delete-key session-id))

(defn delete-all-sessions
  []
  (redis/delete-all-keys))
