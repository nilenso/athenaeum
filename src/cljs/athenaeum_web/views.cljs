(ns athenaeum-web.views
  (:require [re-frame.core :as rf]
            [athenaeum-web.subscriptions :as s]
            [athenaeum-web.events :as e]))

(defn home-page
  []
  (rf/dispatch [::e/fetch-books])
  (fn []
    (let [books @(rf/subscribe [::s/books])]
      [:div
       [:h3 "Books available:"]
       [:ul
        (for [book books]
          ^{:key (:id book)} [:li (:title book)])]])))

(defn page-not-found
  []
  [:div "Page not found."])

(defn root
  []
  (case (:handler @(rf/subscribe [::s/current-page]))
    :home-page [home-page]
    :not-found [page-not-found]
    [page-not-found]))
