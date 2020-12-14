(ns athenaeum-web.views
  (:require [athenaeum-web.routes :as r]))

(defn home-page
  []
  [:div
   [:p "Hello world"]])

(defn page-not-found
  []
  [:div
   [:p "Page not found."]])

(defn root
  []
  (case (get-in @r/app-db [:page :handler])
    :home-page home-page
    page-not-found))
