(ns athenaeum-web.login-page.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [athenaeum-web.login-page.events :as login-page-events]
            [athenaeum-web.app.subscriptions :as subs]
            [athenaeum-web.page.views :as page]))

(defn on-login-success
  [^js google-user]
  (let [id-token (.-id_token (.getAuthResponse google-user))]
    (rf/dispatch [::login-page-events/login id-token])))

(defn on-login-failure
  [_]
  (js/console.error "login failed!"))

(defn login-button
  []
  (r/create-class
   {:component-did-mount (fn []
                           (js/gapi.signin2.render "google-login-id"
                                                   (clj->js {:onsuccess on-login-success
                                                             :onfailure on-login-failure})))
    :display-name        "login-button"
    :reagent-render      (fn []
                           [:div {:id "google-login-id"}])}))

(defn login-page
  []
  (when (= @(rf/subscribe [::subs/login-state]) :logged-out)
    [:div
     [page/navbar]
     [:div.d-flex.align-items-center.flex-column.pt-5
      [:p "Click here to sign in with Google"]
      [login-button]]]))
