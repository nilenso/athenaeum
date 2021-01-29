(ns dev.repl-utils
  (:require [athenaeum.server :as server]
            [athenaeum.config :as config]
            [athenaeum.db :as db]
            [ring.adapter.jetty :as jetty]
            [athenaeum.migrations :as migrations]
            [athenaeum.redis :as redis]
            [athenaeum.handlers.user :as user]))

(defn start-app
  []
  (config/load-config "config/config.dev.edn")
  (user/set-id-token-verifier)
  (redis/set-conn-opts)
  (db/set-datasource)
  (migrations/migrate)
  (reset! server/server (jetty/run-jetty #'server/handler
                                         {:host  "localhost"
                                          :port  (:ring-server-port @config/config)
                                          :join? false})))

(defn stop-app
  []
  (server/stop-app))

(defn restart-app
  []
  (stop-app)
  (start-app))

(defn run-migrations
  [config-file]
  (config/load-config config-file)
  (db/set-datasource)
  (migrations/migrate))
