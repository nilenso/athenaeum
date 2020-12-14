(ns athenaeum.config
  (:require [clojure.java.io :as io]
            [aero.core :as aero]))

(defonce config (atom nil))

(defn load-config
  ([] (load-config "config.edn"))
  ([file-name] (reset! config (aero/read-config
                               (io/resource file-name)))))
