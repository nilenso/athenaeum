(ns athenaeum.fixtures
  (:require [athenaeum.db :as db]
            [athenaeum.config :as config]
            [athenaeum.test-utils :as tu]))

(defn load-config
  [f]
  (let [previous-config @config/config]
    (config/load-config (or (System/getenv "TEST_CONFIG_FILE")
                            "config/config.test.edn"))
    (f)
    (config/reset-config previous-config)))

(defn set-datasource
  [f]
  (let [previous-datasource @db/datasource]
    (db/set-datasource)
    (f)
    (db/reset-datasource previous-datasource)))

(defn clear-tables
  [f]
  (tu/clear-tables)
  (f))
