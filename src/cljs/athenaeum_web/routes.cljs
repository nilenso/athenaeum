(ns athenaeum-web.routes
  (:require [bidi.bidi :as b]
            [pushy.core :as p]))

(def app-db (atom {}))

(def routes
  ["/" {"" :home-page}])

(defn set-page!
  [matched-handler]
  (swap! app-db assoc :page matched-handler))

(def history
  (p/pushy set-page! (partial b/match-route routes)))

(defn init!
  []
  (p/start! history))
