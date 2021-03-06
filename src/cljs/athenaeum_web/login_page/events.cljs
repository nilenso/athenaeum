(ns athenaeum-web.login-page.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]
            [athenaeum-web.routes :as routes]
            [athenaeum-web.app.events :as events]))

(defmethod routes/on-route-change-event
  :login-page
  [_]
  ::login-page-navigated)

(rf/reg-event-fx
 ::login-page-navigated
 (fn [{:keys [db]} _]
   (when (= :logged-in (:login-state db))
     {:fx [[::routes/navigate-to (routes/path-for :home-page)]]})))

(rf/reg-event-fx
 ::login
 (fn [_ [_ id-token]]
   {:http-xhrio {:method          :post
                 :uri             "/api/user/login"
                 :headers         {:Authorization (str "Bearer " id-token)}
                 :timeout         8000
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::login-success]
                 :on-failure      [::login-failure]}}))

(rf/reg-event-fx
 ::login-success
 (fn [{:keys [db]} _]
   {:db (assoc db :login-state :logged-in)
    :fx [[:dispatch [::events/fetch-user]]
         [::routes/navigate-to (routes/path-for :home-page)]]}))

(rf/reg-event-db
 ::login-failure
 (fn [db _]
   (assoc db :login-state :logged-out)))
