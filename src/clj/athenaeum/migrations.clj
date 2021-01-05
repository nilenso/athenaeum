(ns athenaeum.migrations
  (:require [next.jdbc :as jdbc]
            [athenaeum.db :as db]
            [migratus.core :as migratus]))

(defn migration-config
  []
  {:store         :database
   :migration-dir "migrations/"
   :db            {:connection (jdbc/get-connection @db/datasource)}})

(defn create-migration
  [migration-name]
  (migratus/create (migration-config) migration-name))

(defn migrate
  []
  (migratus/migrate (migration-config)))

(defn rollback
  []
  (migratus/rollback (migration-config)))
