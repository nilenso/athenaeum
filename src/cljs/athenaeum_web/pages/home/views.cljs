(ns athenaeum-web.pages.home.views
  (:require [re-frame.core :as rf]
            [athenaeum-web.subscriptions :as s]))

(defn home-page
  []
  (let [books @(rf/subscribe [::s/books])]
    [:div {:class "container"}
     [:h3 "Books available:"]
     [:table {:class "table table-hover"}
      [:thead
       [:tr
        [:th "#"]
        [:th "Name"]
        [:th "Author"]
        [:th "Year"]
        [:th "ISBN"]]]
      [:tbody
       (for [book books]
         ^{:key (:id book)}
         [:tr
          [:td (:id book)]
          [:td (:title book)]
          [:td (:author book)]
          [:td (:year-of-publication book)]
          [:td (:isbn book)]])]]]))
