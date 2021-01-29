(ns athenaeum-web.home-page.subscriptions
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::books
 (fn [db _]
   (->> (:books db)
        (vals)
        (sort-by :id))))
