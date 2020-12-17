(ns dev.repl-utils
  (:require [athenaeum.server :as s]
            [athenaeum.config :as c]))

(defn start-app
  []
  (c/load-config)
  (s/start-app))

(defn restart-app
  []
  (s/stop-app)
  (start-app))
