(ns athenaeum.session.core
  (:require [athenaeum.session.redis :as redis])
  (:import (java.util UUID)))

(defn create-and-get-id
  [user-data]
  (let [session (merge {} user-data)
        session-id (UUID/randomUUID)]
    (redis/set-key session-id session)
    session-id))

(defn fetch
  [session-id]
  (redis/get-value session-id))

(defn delete
  [session-id]
  (redis/delete-key session-id))
