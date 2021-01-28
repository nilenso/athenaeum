(ns athenaeum.domain.user
  (:require [next.jdbc.sql :as sql]))

(defn find-by-id
  [c id]
  (sql/get-by-id c
                 :users
                 id))

(defn find-by-google-id
  [c google-id]
  (first (sql/find-by-keys c
                           :users
                           {:google_id google-id})))

(defn create
  "google id, name, email are non-null values"
  [c user]
  (sql/insert! c
               :users
               {:google_id (:google-id user)
                :name      (:name user)
                :email     (:email user)
                :photo_url (:photo-url user)}))

(defn find-by-google-id-or-create
  [c user]
  (if-let [user (find-by-google-id c (:google-id user))]
    user
    (create c user)))
