(ns athenaeum-web.routes
  (:require [bidi.bidi :as b]
            [pushy.core :as p]
            [re-frame.core :as rf]
            [athenaeum-web.app.events.routing :as routing-events]))

(def routes
  ["/" [["" :home-page]
        ["login" :login-page]
        [true :not-found]]])

(defonce history (atom nil))

(defn set-page
  [page]
  (rf/dispatch [::routing-events/set-current-page page]))

(defn path-for
  [handler]
  (b/path-for routes handler))

(defn init
  []
  (reset! history (p/pushy set-page (partial b/match-route routes)))
  (p/start! @history))
