(ns athenaeum.db
  (:require [next.jdbc :as jdbc]))

(def sql-opts {})

(defonce datasource (atom nil))

(defn set-datasource
  [config]
  (reset! datasource (jdbc/with-options (jdbc/get-datasource (:db-spec @config))
                       sql-opts)))
