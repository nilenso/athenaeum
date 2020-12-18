(ns athenaeum.config
  (:require [clojure.edn :as edn]))

(defonce config (atom nil))

(defn load-config
  [file-name]
  (reset! config (edn/read-string (slurp file-name))))

(defn reset-config
  [new-val]
  (reset! config new-val))
