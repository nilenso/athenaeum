(ns athenaeum.core
  (:require [reagent.dom :as rdom]
            [athenaeum.views :as v]))

(defn root
  []
  [v/home-page])

(defn ^:dev/after-load run []
  (rdom/render [root] (js/document.getElementById "root")))
