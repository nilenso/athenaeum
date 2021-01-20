(ns athenaeum.handlers.book
  (:require [ring.util.response :as response]
            [athenaeum.session :as session]
            [athenaeum.db :as db]
            [athenaeum.domain.book :as books]))

(defn fetch
  [{:keys [cookies]}]
  (let [session-id (get-in cookies [:session-id :value])]
    (if-let [_session (session/fetch session-id)]
      (db/with-transaction [tx @db/datasource]
        (response/response (books/fetch-all tx)))
      (-> (response/response {:message "invalid authentication credentials. login and retry."})
          (response/status 401)))))
