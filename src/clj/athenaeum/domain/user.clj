(ns athenaeum.domain.user
  (:require [next.jdbc.sql :as sql]))

(defn fetch-by-google-id
  [c google-id]
  (first (sql/find-by-keys c
                           :users
                           {:google_id google-id})))

(defn create
  [c user]
  (sql/insert! c
               :users
               {:google_id (:sub user)
                :name      (:name user)
                :email     (:email user)
                :photo_url (:picture user)}))
