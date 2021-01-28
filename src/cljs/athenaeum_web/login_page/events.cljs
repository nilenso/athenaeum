(ns athenaeum-web.login-page.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]
            [athenaeum-web.routes :as routes]
            [athenaeum-web.app.events.routing :as routing-events]
            [athenaeum-web.app.effects :as effects]
            [athenaeum-web.app.events.core :as app-events]))

(defmethod routing-events/on-route-change-event
  :login-page
  [_]
  ::login-page-navigated)

(rf/reg-event-fx
 ::login-page-navigated
 (fn [{:keys [db]} _]
   (when (= :logged-in (:login-state db))
     {:fx [[::effects/navigate-to (routes/path-for :home-page)]]})))

(rf/reg-event-fx
 :redirect-to-login
 (fn [_ _]
   {:fx [[::effects/navigate-to (routes/path-for :login-page)]]}))

(rf/reg-event-fx
 ::login
 (fn [_ [_ id-token]]
   {:http-xhrio {:method          :post
                 :uri             "/api/user/login"
                 :headers         {:Authorization (str "Bearer " id-token)}                          ;{:id-token id-token}
                 :timeout         8000
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::login-success]
                 :on-failure      [::login-failure]}}))

(rf/reg-event-fx
 ::login-success
 (fn [{:keys [db]} _]
   {:db (assoc db :login-state :logged-in)
    :fx [[:dispatch [::app-events/fetch-user]]
         [::effects/navigate-to (routes/path-for :home-page)]]}))

(rf/reg-event-db
 ::login-failure
 (fn [db _]
   (assoc db :login-state :logged-out)))
