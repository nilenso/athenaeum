(ns athenaeum.core
  (:gen-class)
  (:require [athenaeum.server :as s]
            [athenaeum.config :as c]
            [athenaeum.db :as db]))

(defn -main
  [& args]
  (.addShutdownHook (Runtime/getRuntime) (Thread. #(s/stop-app)))
  (if-let [file-name (first args)]
    (c/load-config file-name)
    (c/load-config))
  (db/set-datasource c/config)
  (s/restart-app))
