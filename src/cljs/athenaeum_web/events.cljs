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
