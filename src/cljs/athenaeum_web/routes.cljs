(ns athenaeum-web.routes
  (:require [bidi.bidi :as b]
            [pushy.core :as p]
            [re-frame.core :as rf]
            [athenaeum-web.events :as e]))

(def routes
  ["/" [[""   :home-page]
        [true :not-found]]])

(def page-list
  [{:handler :home-page :page-name "Home"}
   {:handler :nowhere :page-name "Nowhere"}])

(defonce history (atom nil))

(defn set-page
  [page]
  (rf/dispatch [::e/set-current-page page]))

(defn path-for
  [handler]
  (b/path-for routes handler))

(defn init
  []
  (reset! history (p/pushy set-page (partial b/match-route routes)))
  (p/start! @history))
