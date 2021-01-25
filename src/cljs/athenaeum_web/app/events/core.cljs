(ns athenaeum-web.app.events.core
  (:require [re-frame.core :as rf]
            [athenaeum-web.app.db :as db]
            [ajax.core :as ajax]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
 ::auth2-loaded-status
 (fn [db [_ status]]
   (assoc db :auth2-loaded status)))

(rf/reg-event-fx
 ::fetch-user
 (fn [_ [_ on-success on-failure]]
   {:http-xhrio {:method          :get
                 :uri             "/api/user/me"
                 :timeout         8000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::fetch-user-success on-success]
                 :on-failure      [::fetch-user-failure on-failure]}}))

(rf/reg-event-fx
 ::fetch-user-success
 (fn [{:keys [db]} [_ event user]]
   {:db       (-> db
                  (assoc :login-state :logged-in)
                  (assoc :user (:user user)))
    :dispatch (if event [event] [])}))

(rf/reg-event-fx
 ::fetch-user-failure
 (fn [{:keys [db]} [_ event _]]
   {:db       (assoc db :login-state :logged-out)
    :dispatch (if event [event] [])}))
