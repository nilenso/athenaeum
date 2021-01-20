(ns athenaeum.fixtures
  (:require [athenaeum.db :as db]
            [athenaeum.config :as config]
            [athenaeum.redis :as redis]
            [next.jdbc :as jdbc]
            [athenaeum.session :as session]))

(defn load-config
  [f]
  (let [previous-config @config/config]
    (config/load-config (or (System/getenv "TEST_CONFIG_FILE")
                            "config/config.test.edn"))
    (f)
    (config/reset-config previous-config)))

(defn set-datasource
  [f]
  (let [previous-datasource @db/datasource]
    (db/set-datasource)
    (f)
    (db/reset-datasource previous-datasource)))

(defn set-redis-server-conn
  [f]
  (let [previous-conn @redis/server-conn]
    (redis/set-conn-opts)
    (f)
    (redis/reset-conn previous-conn)))

(defn- truncate-all-tables
  []
  (jdbc/execute! @db/datasource ["TRUNCATE TABLE books CASCADE"])
  (jdbc/execute! @db/datasource ["TRUNCATE TABLE users CASCADE"]))

(defn clear-tables
  [f]
  (truncate-all-tables)
  (f))

(defn clear-sessions
  [f]
  (session/delete-all-sessions)
  (f))
