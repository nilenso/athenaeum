(ns athenaeum-web.page.views
  (:require [re-frame.core :as rf]
            [athenaeum-web.app.subscriptions :as subs]
            [athenaeum-web.routes :as routes]
            [athenaeum-web.page.events :as events]))

(defn- on-logout
  [_]
  (.then (.signOut (js/gapi.auth2.getAuthInstance))
         #(rf/dispatch [::events/logout])
         #(js/console.error "logout failed!")))

(defn- logout-button
  []
  [:button.btn.btn-outline-secondary
   {:on-click on-logout
    :disabled (not @(rf/subscribe [::subs/auth2-loaded]))}
   "Log out"])

(defn- page-link
  [handler page-name]
  [:a {:class (str "nav-item nav-link pr-3"
                   (when
                    (= handler
                       (:handler @(rf/subscribe [::subs/current-page])))
                     " active"))
       :href  (routes/path-for handler)} page-name])

(defn navbar
  []
  [:nav.navbar.navbar-light.bg-light.flex-row
   [:a.navbar-brand {:href (routes/path-for :home-page)} "Athenaeum"]
   [:div.navbar-nav.flex-row
    [page-link :home-page "Home"]
    (when (= @(rf/subscribe [::subs/login-state]) :logged-in) [logout-button])]])
