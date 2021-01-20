(ns athenaeum-web.app.events
  (:require [re-frame.core :as rf]
            [athenaeum-web.app.db :as db]
            [ajax.core :as ajax]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

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
 ::session
 (fn [_ _]
   {:http-xhrio {:method          :get
                 :uri             "/api/me"
                 :timeout         8000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::session-exists]
                 :on-failure      [::session-absent]}}))

(rf/reg-event-db
 ::session-exists
 (fn [db _]
   (assoc db :login-state :logged-in)))

(rf/reg-event-db
 ::session-absent
 (fn [db _]
   (assoc db :login-state :logged-out)))
