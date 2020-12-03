(ns athenaeum.core
  (:require [reagent.dom :as rdom]
            [athenaeum.views :as v]))

(defn root
  []
  [v/home-page])

(defn run []
  (rdom/render [root] (js/document.getElementById "root")))
