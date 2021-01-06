(ns athenaeum-web.events
  (:require [re-frame.core :as rf]
            [athenaeum-web.db :as db]
            [athenaeum-web.pages.home.events :as home]))

(def page-navigation-map
  {:home-page ::home/home-page-navigated})

(rf/reg-event-fx
 ::set-current-page
 (fn [{:keys [db]} [_ route]]
   {:db (assoc db :page route)
    :fx (if-let [event ((:handler route) page-navigation-map)]
          [[:dispatch [event]]]
          [])}))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
 ::signin-user
 (fn [db [_ name email photo id-token]]
   (-> db
       (assoc :user {:name     name
                     :email    email
                     :photo    photo
                     :id-token id-token})
       (assoc :signin-state :signed-in))))

(rf/reg-event-db
 ::signout-user
 (fn [_ _]
   db/default-db))
