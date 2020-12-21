(ns athenaeum.handlers.book
  (:require [ring.util.response :as response]
            [athenaeum.domain.book :as books]
            [next.jdbc :as jdbc]
            [athenaeum.db :as db]))

(defn fetch
  [_]
  (jdbc/with-transaction [tx @db/datasource]
    (response/response (books/fetch-all tx))))
