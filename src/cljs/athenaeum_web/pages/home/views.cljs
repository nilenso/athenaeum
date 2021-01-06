(ns athenaeum-web.pages.home.views
  (:require [re-frame.core :as rf]
            [athenaeum-web.subscriptions :as s]
            [athenaeum-web.pages.views :as p]
            [athenaeum-web.events :as e]
            [reagent.core :as r]))

(defn on-signin-success
  [^js google-user]
  (let [profile (.getBasicProfile google-user)
        name (.getName profile)
        email (.getEmail profile)
        photo (.getImageUrl profile)
        id-token (.-id_token (.getAuthResponse google-user))]
    (rf/dispatch [::e/signin-user name email photo id-token])))

(defn on-signin-failure
  [_]
  (js/console.error "sign-in failed!"))

(defn signin-button
  []
  (r/create-class
   {:component-did-mount (fn []
                           (js/gapi.signin2.render "google-signin-id"
                                                   (clj->js {:onsuccess on-signin-success
                                                             :onfailure on-signin-failure})))
    :display-name        "signin-button"
    :reagent-render      (fn []
                           [:div {:id "google-signin-id"}])}))

(defn on-signout
  [_]
  (.then (.signOut (js/gapi.auth2.getAuthInstance))
         #(rf/dispatch [::e/signout-user])
         #(js/console.error "sign-out failed!")))

(defn signout-button
  []
  [:a {:href "#" :onClick on-signout} "Sign out"])

(defn books-table
  []
  (let [books @(rf/subscribe [::s/books])]
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
  (if (= @(rf/subscribe [::s/signin-state]) :signed-in)
    [:div
     [p/navbar]
     [signout-button]
     [:div.container.pt-5
      [:h3 "Books available:"]
      [:br]
      [books-table]]]
    [signin-button]))
