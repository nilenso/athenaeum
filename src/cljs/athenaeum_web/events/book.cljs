(ns athenaeum-web.events.book
  (:require [re-frame.core :as rf]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]))

(defn- map-id-to-value
  [values]
  (zipmap (map :id values) values))

(rf/reg-event-db
 ::fetch-books-success
 (fn [db [_ books]]
   (assoc db :books (map-id-to-value books))))

(rf/reg-event-db
 ::fetch-books-failure
 (fn [db _]
   db))

(rf/reg-event-fx
 ::fetch-books
 (fn [_ _]
   {:http-xhrio {:method          :get
                 :uri             "/api/books"
                 :timeout         8000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::fetch-books-success]
                 :on-failure      [::fetch-books-failure]}}))
