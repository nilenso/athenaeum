(ns athenaeum-web.home-page.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [athenaeum-web.routes :as routes]
            [athenaeum-web.app.events :as e]
            [athenaeum-web.app.effects :as effects]))

(defn- map-id-to-value
  [values]
  (zipmap (map :id values) values))

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
   (assoc db :books (map-id-to-value books))))

(rf/reg-event-db
 ::fetch-books-failure
 (fn [db _]
   db))

(defmethod e/on-route-change-event
  :home-page
  [_]
  ::home-page-navigated)

(rf/reg-event-fx
 ::home-page-navigated
 (fn [{:keys [db]} _]
   (if (= (:login-state db) :logged-in)
     {:dispatch [::fetch-books]}
     {:fx [[::effects/navigate-to (routes/path-for :login-page)]]})))
