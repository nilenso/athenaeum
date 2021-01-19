(ns athenaeum-web.app.events
  (:require [re-frame.core :as rf]
            [athenaeum-web.app.db :as db]))

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
