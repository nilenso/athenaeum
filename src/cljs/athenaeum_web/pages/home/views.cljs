(ns athenaeum-web.pages.home.views
  (:require [re-frame.core :as rf]
            [athenaeum-web.subscriptions :as s]))

(defn home-page
  []
  (let [books @(rf/subscribe [::s/books])]
    [:div
     [:h3 "Books available:"]
     [:ul
      (for [book books]
        ^{:key (:id book)} [:li (:title book)])]]))
