(ns athenaeum.fixtures
  (:require [next.jdbc :as jdbc]
            [clojure.edn :as edn]
            [athenaeum.db :as db]))

(defonce test-config (atom nil))

(defn config-and-datasource
  [f]
  (reset! test-config (edn/read-string (slurp "config/config.test.edn")))
  (db/set-datasource test-config)
  (f))

(defn clear-tables
  [f]
  (jdbc/execute! @db/datasource ["TRUNCATE TABLE books CASCADE"])
  (f))
