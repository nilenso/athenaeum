(ns athenaeum-web.login-page.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]))

(rf/reg-event-fx
 ::login
 (fn [_ [_ id-token]]
   {:http-xhrio {:method          :post
                 :uri             "/api/login"
                 :headers         {:id-token id-token}
                 :timeout         8000
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::login-success]
                 :on-failure      [::login-failure]}}))

(rf/reg-event-db
 ::login-success
 (fn [db _]
   (assoc db :login-state :logged-in)))

(rf/reg-event-db
 ::login-failure
 (fn [db _]
   (assoc db :login-state :logged-out)))

(rf/reg-event-db
 ::add-id-token
 (fn [db [_ id-token]]
   (assoc-in db [:user :id-token] id-token)))
