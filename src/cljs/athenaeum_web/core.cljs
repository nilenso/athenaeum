(ns athenaeum-web.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [athenaeum-web.app.views :as views]
            [athenaeum-web.app.events.core :as events]
            [athenaeum-web.routes :as routes]
            [athenaeum-web.home-page.events]
            [athenaeum-web.app.effects]
            [day8.re-frame.http-fx]))

(defn render
  []
  (rdom/render [views/root] (js/document.getElementById "root")))

(defn ^:dev/after-load rerun
  []
  (rf/clear-subscription-cache!)
  (render))

(defn load-auth2-library
  []
  (.load js/gapi "auth2"
         (clj->js {:callback (fn []
                               (.init js/gapi.auth2 (clj->js {:client_id "391229463438-a0vus34lglv9tsggpbmq2l1ekfb2lc3s.apps.googleusercontent.com"}))
                               (rf/dispatch [::events/auth2-loaded-status true]))
                   :timeout 3000
                   :ontimeout #(js/error "gapi.auth2 failed to load!")})))

(defn run
  []
  (rf/dispatch-sync [::events/initialize-db])
  (load-auth2-library)
  (routes/init)
  (render))
