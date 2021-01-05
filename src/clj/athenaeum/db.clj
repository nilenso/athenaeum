(ns athenaeum.db
  (:require [next.jdbc :as jdbc]
            [camel-snake-kebab.core]
            [next.jdbc.result-set :as jdbc-result-set]
            [athenaeum.config :as config]))

(def sql-opts {:builder-fn jdbc-result-set/as-unqualified-kebab-maps})

(defonce datasource (atom nil))

(defn set-datasource
  []
  (reset! datasource (jdbc/with-options (jdbc/get-datasource (:db-spec @config/config))
                       sql-opts)))

(defn reset-datasource
  [new-val]
  (reset! datasource new-val))

(defmacro with-transaction
  [[transaction datasource] & body]
  `(jdbc/with-transaction [transaction# ~datasource]
     (let [~transaction (jdbc/with-options transaction# sql-opts)]
       ~@body)))
