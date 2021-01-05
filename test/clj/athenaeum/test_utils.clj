(ns athenaeum.test-utils
  (:require [next.jdbc :as jdbc]
            [athenaeum.db :as db]))

(defn clear-tables
  []
  (jdbc/execute! @db/datasource ["TRUNCATE TABLE books"]))
