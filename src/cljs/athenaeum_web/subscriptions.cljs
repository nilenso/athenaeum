(ns athenaeum-web.subscriptions
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::current-page
 (fn [db _]
   (get db :page)))

(rf/reg-sub
 ::books
 (fn [db _]
   (->> (:books db)
        (vals)
        (sort-by :title))))
