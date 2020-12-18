(ns athenaeum.db
  (:require [next.jdbc :as jdbc]
            [athenaeum.config :as config]))

(def sql-opts {})

(defonce datasource (atom nil))

(defn set-datasource
  []
  (reset! datasource (jdbc/with-options (jdbc/get-datasource (:db-spec @config/config))
                       sql-opts)))

(defn reset-datasource
  [new-val]
  (reset! datasource new-val))
