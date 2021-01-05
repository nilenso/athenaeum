(ns athenaeum-web.pages.home.views
  (:require [re-frame.core :as rf]
            [athenaeum-web.subscriptions :as s]
            [athenaeum-web.pages.views :as p]))

(defn home-page
  []
  (let [books @(rf/subscribe [::s/books])]
    [:div
     [p/navbar]
     [:div.container.pt-5
      [:h3 "Books available:"]
      [:br]
      [:table.table.table-hover
       [:thead
        [:tr
         [:th "Name"]
         [:th "Author"]
         [:th "Year"]
         [:th "ISBN"]]]
       [:tbody
        (for [book books]
          ^{:key (:id book)}
          [:tr
           [:td (:title book)]
           [:td (:author book)]
           [:td (:year-of-publication book)]
           [:td (:isbn book)]])]]]]))
