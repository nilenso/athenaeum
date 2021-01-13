(ns athenaeum.domain.user
  (:require [next.jdbc.sql :as sql]))

(defn fetch-one
  [c google-id]
  (first (sql/query c
                    ["SELECT * FROM users WHERE google_id=?" google-id])))

(defn create
  [c user]
  (sql/insert! c
               :users
               {:google_id (:sub user)
                :name      (:name user)
                :email     (:email user)
                :photo_url (:picture user)}))
