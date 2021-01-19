(ns athenaeum-web.page.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]
            [athenaeum-web.app.db  :as db]
            [athenaeum-web.routes :as routes]))

(rf/reg-event-fx
 ::logout
 (fn [_ _]
   {:http-xhrio {:method          :get
                 :uri             "/api/logout"
                 :timeout         8000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::logout-success]
                 :on-failure      [::logout-failure]}}))

(rf/reg-event-fx
 ::logout-success
 (fn [_ _]
   {:db db/default-db
    :fx [[:navigate-to (routes/path-for :home-page)]]}))

(rf/reg-event-db
 ::logout-failure
 (fn [db _]
   db))
