(ns athenaeum.session
  (:require [athenaeum.redis :as redis])
  (:import (java.util UUID)))

(defn create-and-get-id
  [user-data]
  (let [session user-data
        session-id (str (UUID/randomUUID))]
    (redis/set-key session-id session)
    session-id))

(defn exists?
  [session-id]
  (redis/key-exists? session-id))

(defn fetch
  [session-id]
  (redis/get-value session-id))

(defn delete
  [session-id]
  (redis/delete-key session-id))
