(ns athenaeum-web.app.events.routing
  (:require [re-frame.core :as rf]
            [athenaeum-web.app.events.core :as events]))

(defmulti on-route-change-event :handler :default ::default)

(defmethod on-route-change-event
  ::default
  [_]
  nil)

(def authenticated-pages
  #{:home-page})

(defn authentication-check
  [route login-state page-navigated-event]
  (if (contains? authenticated-pages (:handler route))
    (if (= login-state :logged-in)
      [page-navigated-event]
      [::events/fetch-user page-navigated-event :redirect-to-login])
    (if page-navigated-event
      [page-navigated-event]
      [])))

(rf/reg-event-fx
 ::set-current-page
 (fn [{:keys [db]} [_ route]]
   {:db (assoc db :page route)
    :fx (let [event (authentication-check route (:login-state db) (on-route-change-event route))]
          (if (empty? event)
            []
            [[:dispatch event]]))}))
