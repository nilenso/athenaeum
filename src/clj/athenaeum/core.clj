(ns clj.athenaeum.core
  (:gen-class)
  (:require [clj.athenaeum.server :as s]
            [clj.athenaeum.config :as c]))

(defn -main
  [& args]
  (.addShutdownHook (Runtime/getRuntime) (Thread. #(s/stop-app)))
  (if-let [file-name (first args)]
    (c/load-config file-name)
    (c/load-config))
  (s/start-app))
