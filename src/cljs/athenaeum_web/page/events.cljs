(ns athenaeum-web.page.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]
            [athenaeum-web.app.db  :as db]
            [athenaeum-web.routes :as routes]))

(rf/reg-event-fx
 ::logout
 (fn [_ _]
   {:http-xhrio {:method          :get
                 :uri             "/api/user/logout"
                 :timeout         8000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::logout-success]
                 :on-failure      [::logout-failure]}}))

(rf/reg-event-fx
 ::logout-success
 (fn [{:keys [db]} _]
   {:db (assoc db/default-db :auth2-loaded (:auth2-loaded db))
    :fx [[::routes/navigate-to (routes/path-for :login-page)]]}))

(rf/reg-event-db
 ::logout-failure
 (fn [db _]
   db))
