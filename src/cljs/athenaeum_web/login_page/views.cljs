(ns athenaeum-web.login-page.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [athenaeum-web.login-page.events :as e]
            [athenaeum-web.app.subscriptions :as s]
            [athenaeum-web.page.views :as p]))

(defn on-login-success
  [^js google-user]
  (let [profile (.getBasicProfile google-user)
        name (.getName profile)
        email (.getEmail profile)
        photo (.getImageUrl profile)
        id-token (.-id_token (.getAuthResponse google-user))]
    (rf/dispatch [::e/login-user name email photo id-token])))

(defn on-login-failure
  [_]
  (js/console.error "sign-in failed!"))

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
  (if (= @(rf/subscribe [::s/login-state]) :logged-out)
    [:div
     [p/navbar]
     [:div.d-flex.align-items-center.flex-column.pt-5
      [:p "Click here to sign in with Google"]
      [login-button]]]
    [:div
     [p/navbar]
     [:div.text-center.pt-5
      [:p "You've logged in! Click here to go home"]
      [:a.btn.btn-primary {:href "/" :role "button"} "Home"]]]))
