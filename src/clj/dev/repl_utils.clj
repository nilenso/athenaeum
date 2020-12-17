(ns dev.repl-utils
  (:require [athenaeum.server :as s]
            [athenaeum.config :as c]
            [athenaeum.db :as db]))

(defn start-app
  []
  (c/load-config)
  (db/set-datasource c/config)
  (s/start-app))

(defn restart-app
  []
  (s/stop-app)
  (start-app))
