(ns athenaeum-web.views
  (:require [re-frame.core :as rf]
            [athenaeum-web.subscriptions :as s]))

(defn home-page
  []
  [:div "Hello world"])

(defn page-not-found
  []
  [:div "Page not found."])

(defn root
  []
  (case (:handler @(rf/subscribe [::s/current-page]))
    :home-page [home-page]
    [page-not-found]))
