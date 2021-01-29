(ns athenaeum.config
  (:require [aero.core :as aero]))

(defonce config (atom nil))

(defn load-config
  [file-name]
  (reset! config (aero/read-config file-name)))

(defn reset-config
  [new-val]
  (reset! config new-val))
