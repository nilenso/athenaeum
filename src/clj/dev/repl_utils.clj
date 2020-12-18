(ns dev.repl-utils
  (:require [athenaeum.server :as server]
            [athenaeum.config :as config]
            [athenaeum.db :as db]
            [ring.adapter.jetty :as jetty]))

(defn start-app
  []
  (config/load-config "config/config.dev.edn")
  (db/set-datasource)
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
