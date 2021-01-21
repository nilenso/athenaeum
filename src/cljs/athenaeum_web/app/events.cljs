(ns athenaeum-web.app.events
  (:require [re-frame.core :as rf]
            [athenaeum-web.app.db :as db]
            [ajax.core :as ajax]))

(rf/reg-event-fx
 ::initialize-db
 (fn [_ _]
   {:db       db/default-db
    :dispatch [::fetch-user]}))

(defmulti on-route-change-event :handler :default ::default)

(defmethod on-route-change-event
  ::default
  [_]
  nil)

(rf/reg-event-fx
 ::set-current-page
 (fn [{:keys [db]} [_ route]]
   {:db (assoc db :page route)
    :fx (if-let [event (on-route-change-event route)]
          [[:dispatch [event]]]
          [])}))

(rf/reg-event-fx
 ::fetch-user
 (fn [_ _]
   {:http-xhrio {:method          :get
                 :uri             "/api/user/me"
                 :timeout         8000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::fetch-user-success]
                 :on-failure      [::fetch-user-failure]}}))

(rf/reg-event-db
 ::fetch-user-success
 (fn [db [_ user]]
   (-> db
       (assoc :login-state :logged-in)
       (assoc :user (:user user)))))

(rf/reg-event-db
 ::fetch-user-failure
 (fn [db _]
   (assoc db :login-state :logged-out)))
