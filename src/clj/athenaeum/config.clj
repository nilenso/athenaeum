(ns athenaeum.config
  (:require [aero.core :as aero]))

(defonce config (atom nil))

(defn load-config
  ([] (load-config "config/config.edn"))
  ([file-name] (reset! config (aero/read-config file-name))))
