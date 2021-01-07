(ns athenaeum-web.login-page.events
  (:require [re-frame.core :as rf]))

(rf/reg-event-db
 ::login-user
 (fn [db [_ name email photo id-token]]
   (-> db
       (assoc :user {:name     name
                     :email    email
                     :photo    photo
                     :id-token id-token})
       (assoc :login-state :logged-in))))
