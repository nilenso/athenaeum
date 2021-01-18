(ns athenaeum-web.page.views
  (:require [re-frame.core :as rf]
            [athenaeum-web.app.subscriptions :as s]
            [athenaeum-web.routes :as r]
            [athenaeum-web.app.events :as e]))

(defn on-logout
  [_]
  (.then (.signOut (js/gapi.auth2.getAuthInstance))
         #(rf/dispatch [::e/logout-user])
         #(js/console.error "logout failed!")))

(defn logout-button
  []
  [:button.btn.btn-outline-secondary {:on-click on-logout} "Log out"])

(defn- page-link
  [handler page-name]
  [:a {:class (str "nav-item nav-link pr-3"
                   (when
                    (= handler
                       (:handler @(rf/subscribe [::s/current-page])))
                     " active"))
       :href  (r/path-for handler)} page-name])

(defn navbar
  []
  [:nav.navbar.navbar-light.bg-light.flex-row
   [:a.navbar-brand {:href (r/path-for :home-page)} "Athenaeum"]
   [:div.navbar-nav.flex-row
    [page-link :home-page "Home"]
    (when (= @(rf/subscribe [::s/login-state]) :logged-in) [logout-button])]])
