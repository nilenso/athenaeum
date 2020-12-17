(ns athenaeum.books.handlers
  (:require [ring.util.response :as response]
            [athenaeum.books.db :as books]
            [next.jdbc :as jdbc]
            [athenaeum.db :as db]))

(defn fetch
  [_]
  (jdbc/with-transaction [tx @db/datasource]
    (response/response (str (books/fetch-all tx)))))

(defn create
  [{:keys [params]}]
  (jdbc/with-transaction [tx @db/datasource]
    (response/response (str (books/create tx
                                          (:name params)
                                          (:author params))))))
