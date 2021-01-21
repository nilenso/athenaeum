(ns athenaeum-web.login-page.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]
            [athenaeum-web.routes :as routes]
            [athenaeum-web.app.events :as e]))

(defmethod e/on-route-change-event
  :login-page
  [_]
  ::login-page-navigated)

(rf/reg-event-fx
 ::login-page-navigated
 (fn [{:keys [db]} _]
   (when (= :logged-in (:login-state db))
     {:fx [[:navigate-to (routes/path-for :home-page)]]})))

(rf/reg-event-fx
 ::login
 (fn [_ [_ id-token]]
   {:http-xhrio {:method          :post
                 :uri             "/api/user/login"
                 :headers         {:id-token id-token}
                 :timeout         8000
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::login-success]
                 :on-failure      [::login-failure]}}))

(rf/reg-event-fx
 ::login-success
 (fn [{:keys [db]} _]
   {:db (assoc db :login-state :logged-in)
    :fx [[:navigate-to (routes/path-for :home-page)]]}))

(rf/reg-event-db
 ::login-failure
 (fn [db _]
   (assoc db :login-state :logged-out)))

(rf/reg-event-db
 ::add-id-token
 (fn [db [_ id-token]]
   (assoc-in db [:user :id-token] id-token)))
