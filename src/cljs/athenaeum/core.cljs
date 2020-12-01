(ns athenaeum.core
  (:require [reagent.dom :as rdom]))

(defonce page-state (atom nil))

(defn hello-world-component
  []
  [:div
   [:h1 "Hello world"]])

(defn run []
  (rdom/render [hello-world-component] (js/document.getElementById "app")))
