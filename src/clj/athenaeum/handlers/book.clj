(ns athenaeum.handlers.book
  (:require [ring.util.response :as response]
            [athenaeum.session :as session]
            [athenaeum.db :as db]
            [athenaeum.domain.book :as books]))

(defn fetch
  [{:keys [cookies]}]
  (let [session-id (get-in cookies [:session-id :value])]
    (if (session/fetch session-id)
      (try (db/with-transaction [tx @db/datasource]
             (response/response (books/fetch-all tx)))
           (catch Exception _
             (-> (response/response {:message "Internal server error"})
                 (response/status 500))))
      (-> (response/response {:message "invalid authentication credentials. login and retry."})
          (response/status 401)))))
