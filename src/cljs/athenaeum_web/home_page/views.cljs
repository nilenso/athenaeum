(ns athenaeum-web.home-page.views
  (:require [re-frame.core :as rf]
            [athenaeum-web.app.subscriptions :as subs]
            [athenaeum-web.home-page.subscriptions :as home-page-subs]
            [athenaeum-web.page.views :as p]))

(defn books-table
  []
  (let [books @(rf/subscribe [::home-page-subs/books])]
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
         [:td (:isbn book)]])]]))

(defn home-page
  []
  (if (= @(rf/subscribe [::subs/login-state]) :logged-in)
    [:div
     [p/navbar]
     [:div.container.pt-5
      [:h3 "Books available:"]
      [:br]
      [books-table]]]
    [p/navbar]))
