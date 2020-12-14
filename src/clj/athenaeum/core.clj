(ns athenaeum.core
  (:gen-class)
  (:require [athenaeum.server :as s]
            [athenaeum.config :as c]))

(defn -main
  [& args]
  (.addShutdownHook (Runtime/getRuntime) (Thread. #(s/stop-app)))
  (if-let [file-name (first args)]
    (c/load-config file-name)
    (c/load-config))
  (s/restart-app))
