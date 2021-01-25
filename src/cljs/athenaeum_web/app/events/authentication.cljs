(ns athenaeum-web.app.events.authentication
  (:require [re-frame.core :as rf]
            [athenaeum-web.routes :as routes]
            [athenaeum-web.app.effects :as effects]
            [athenaeum-web.app.events.core :as events]))

(rf/reg-event-fx
 ::redirect-to-login
 (fn [_ _]
   {:fx [[::effects/navigate-to (routes/path-for :login-page)]]}))

(rf/reg-event-fx
 ::authentication-check
 (fn [{:keys [db]} [_ on-success]]
   (if (= (:login-state db) :logged-in)
     {:dispatch [on-success]}
     {:dispatch [::events/fetch-user on-success ::redirect-to-login]})))
