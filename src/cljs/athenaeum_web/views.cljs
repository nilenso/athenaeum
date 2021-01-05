(ns athenaeum-web.views
  (:require [re-frame.core :as rf]
            [athenaeum-web.subscriptions :as s]
            [athenaeum-web.pages.home.views :as home]))

(defn root
  []
  (case (:handler @(rf/subscribe [::s/current-page]))
    :home-page [home/home-page]
    :not-found [:div "Page not found."]
    [:div "Page not found."]))
