(ns athenaeum-web.app.views
  (:require [re-frame.core :as rf]
            [athenaeum-web.app.subscriptions :as subs]
            [athenaeum-web.home-page.views :as home]
            [athenaeum-web.login-page.views :as login]
            [athenaeum-web.page.views :as page]))

(defn not-found-page
  []
  [:div
   [page/navbar]
   [:div.text-center.pt-5
    [:p "Page not found."]
    [:a.btn.btn-primary {:href "/" :role "button"} "Go home"]]])

(defn root
  []
  (case (:handler @(rf/subscribe [::subs/current-page]))
    :home-page [home/home-page]
    :login-page [login/login-page]
    :not-found [not-found-page]
    [not-found-page]))
