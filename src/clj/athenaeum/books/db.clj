(ns athenaeum.books.db
  (:require [next.jdbc.sql :as sql]))

(defn create
  [c title author]
  (sql/insert! c
               :books
               {:title  title
                :author author}))

(defn fetch-all
  [c]
  (sql/query c
             ["SELECT * FROM books"]))
