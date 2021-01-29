(ns athenaeum.redis
  (:require [taoensso.carmine :as car]
            [athenaeum.config :as config]))

(defonce server-conn (atom nil))

(defn set-conn-opts
  []
  (reset! server-conn {:pool {}, :spec {:host (:redis-host @config/config)}}))

(defn reset-conn
  [new-val]
  (reset! server-conn new-val))

(defmacro wcar*
  [& body]
  `(car/wcar @server-conn ~@body))

(defn set-key
  ([key value]
   (set-key key value 3600))
  ([key value ttl]
   (wcar* (car/set key value)
          (car/expire key ttl))))

(defn key-exists?
  [key]
  (= 1 (wcar* (car/exists key))))

(defn get-value
  [key]
  (wcar* (car/get key)))

(defn delete-key
  [key]
  (wcar* (car/del key)))

(defn delete-all-keys
  []
  (wcar* (car/flushall)))


