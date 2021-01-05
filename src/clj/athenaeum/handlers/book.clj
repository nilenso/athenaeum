(ns athenaeum.handlers.book
  (:require [ring.util.response :as response]
            [athenaeum.domain.book :as books]
            [athenaeum.db :as db]))

(defn fetch
  [_]
  (db/with-transaction [tx @db/datasource]
    (response/response (books/fetch-all tx))))
