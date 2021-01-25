(ns athenaeum-web.app.events.routing
  (:require [re-frame.core :as rf]))

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
