(ns athenaeum-web.core
  (:require [reagent.dom :as rdom]
            [athenaeum-web.views :as v]
            [athenaeum-web.routes :as routes]
            [re-frame.core :as rf]
            [athenaeum-web.events :as e]))

(defn render
  []
  (rdom/render [v/root] (js/document.getElementById "root")))

(defn ^:dev/after-load rerun
  []
  (rf/clear-subscription-cache!)
  (render))

(defn run
  []
  (rf/dispatch-sync [::e/initialize-db])
  (routes/init)
  (render))
