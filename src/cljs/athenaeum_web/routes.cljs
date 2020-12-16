(ns athenaeum-web.routes
  (:require [bidi.bidi :as b]
            [pushy.core :as p]
            [re-frame.core :as rf]
            [athenaeum-web.events :as e]))

(def routes
  ["/" {"" :home-page}])

(defonce history (atom nil))

(defn set-page
  [page]
  (rf/dispatch [::e/set-current-page page]))

(defn init
  []
  (reset! history (p/pushy set-page (partial b/match-route routes)))
  (p/start! @history))
