(ns athenaeum.domain.user
  (:require [next.jdbc.sql :as sql]))

(defn fetch-by-google-id
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
