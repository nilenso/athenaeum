(ns athenaeum-web.events
  (:require [re-frame.core :as rf]
            [athenaeum-web.db :as db]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]))

(rf/reg-event-db
 ::set-current-page
 (fn [db [_ current-page]]
   (assoc db :page current-page)))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
 ::fetch-books-success
 (fn [db [_ books]]
   (assoc db :books (zipmap (map :books/id books) books))))

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
