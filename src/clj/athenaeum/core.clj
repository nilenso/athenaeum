(ns athenaeum.core
  (:gen-class)
  (:require [athenaeum.server :as server]
            [athenaeum.config :as config]
            [athenaeum.db :as db]
            [athenaeum.redis :as redis]
            [athenaeum.migrations :as migrations]))

(defn -main
  [& args]
  (config/load-config (first args))
  (db/set-datasource)
  (redis/set-conn-opts)
  (migrations/migrate)
  (when-not (= "migrate" (second args))
    (server/restart-app)))
