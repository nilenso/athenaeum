(ns athenaeum.config
  (:require [clojure.java.io :as io]
            [aero.core :as aero]))

(defonce config (atom nil))

(defn load-config
  ([] (load-config "config.edn"))
  ([file-name]
   (if-let [config-file (io/resource file-name)]
     (reset! config (aero/read-config config-file))
     (reset! config (aero/read-config file-name)))))
