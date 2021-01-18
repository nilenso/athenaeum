(ns athenaeum.handlers.book
  (:require [ring.util.response :as response]
            [athenaeum.session.core :as session]
            [athenaeum.db :as db]
            [athenaeum.domain.book :as books]))

(defn fetch
  [{:keys [cookies]}]
  (if-let [session-id (get cookies "session-id")]
    (if-let [_session (session/fetch session-id)]
      (db/with-transaction [tx @db/datasource]
        (response/response (books/fetch-all tx)))
      (-> (response/response {:message "Invalid authentication credentials. Please login and try again."})
          (response/status 401)))
    (response/bad-request {:message "Session id cookie missing"})))
