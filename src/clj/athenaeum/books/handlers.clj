(ns athenaeum.books.handlers
  (:require [ring.util.response :as response]
            [athenaeum.books.db :as books]
            [next.jdbc :as jdbc]
            [athenaeum.db :as db]))

(defn create
  [{:keys [params]}]
  (jdbc/with-transaction [tx @db/datasource]
    (response/response (books/create tx
                                     (:name params)
                                     (:author params)))))

(defn fetch
  [_]
  (jdbc/with-transaction [tx @db/datasource]
    (response/response (books/fetch-all tx))))
