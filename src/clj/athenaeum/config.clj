(ns clj.athenaeum.config
  (:require [clojure.edn :as edn]))

(defonce config (atom nil))

(defn load-config
  ([] (load-config "resources/config.edn"))
  ([file-name] (reset! config (edn/read-string (slurp file-name)))))
