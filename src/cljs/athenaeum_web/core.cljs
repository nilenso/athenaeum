(ns athenaeum-web.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [athenaeum-web.app.views :as v]
            [athenaeum-web.app.events :as e]
            [athenaeum-web.routes :as routes]))

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
