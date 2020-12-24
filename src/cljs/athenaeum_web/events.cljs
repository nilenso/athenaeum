(ns athenaeum-web.events
  (:require [re-frame.core :as rf]
            [athenaeum-web.db :as db]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]))

(def page-navigation-map
  {:home-page ::home-page-navigated})

(rf/reg-event-fx
 ::set-current-page
 (fn [{:keys [db]} [_ route]]
   {:db (assoc db :page route)
    :fx (if-let [event ((:handler route) page-navigation-map)]
          [[:dispatch [event]]]
          [])}))

(rf/reg-event-fx
 ::home-page-navigated
 (fn [_ _]
   {:fx [[:dispatch [::fetch-books]]]}))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

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
