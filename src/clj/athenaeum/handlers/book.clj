(ns athenaeum.handlers.book
  (:require [ring.util.response :as response]
            [athenaeum.db :as db]
            [athenaeum.domain.book :as books]))

(defn fetch
  [_]
  (db/with-transaction [tx @db/datasource]
    (response/response (books/fetch-all tx))))
