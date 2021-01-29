(ns athenaeum-web.home-page.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]
            [athenaeum-web.utils :as utils]
            [athenaeum-web.routes :as routes]))

(defmethod routes/on-route-change-event
  :home-page
  [_]
  ::home-page-navigated)

(rf/reg-event-fx
 ::home-page-navigated
 (fn [_ _]
   {:dispatch [::fetch-books]}))

(rf/reg-event-fx
 ::fetch-books
 (fn [_ _]
   {:http-xhrio {:method          :get
                 :uri             "/api/books"
                 :timeout         8000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::fetch-books-success]
                 :on-failure      [::fetch-books-failure]}}))

(rf/reg-event-db
 ::fetch-books-success
 (fn [db [_ books]]
   (assoc db :books (utils/map-id-to-value books))))

(rf/reg-event-db
 ::fetch-books-failure
 (fn [db _]
   db))
