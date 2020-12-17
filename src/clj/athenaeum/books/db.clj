(ns athenaeum.books.db
  (:require [next.jdbc.sql :as sql]))

(defn fetch-all
  [c]
  (sql/query c
             ["SELECT * FROM books"]))

(defn create
  [c name author]
  (sql/insert! c
               :books
               {:name   name
                :author author}))
