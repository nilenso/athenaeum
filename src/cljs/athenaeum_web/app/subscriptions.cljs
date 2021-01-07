(ns athenaeum-web.app.subscriptions
  (:require [re-frame.core :as rf]))

(rf/reg-sub
  ::current-page
  (fn [db _]
    (get db :page)))

(rf/reg-sub
  ::login-state
  (fn [db _]
    (get db :login-state)))
