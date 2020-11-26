(ns clj.athenaeum.server
  (:require [ring.adapter.jetty :as r]
            [clj.athenaeum.handlers :as h]
            [clj.athenaeum.config :as c]
            [bidi.bidi :as b]
            [bidi.ring :refer (make-handler)]))

(defonce server (atom nil))

(def routes
  ["/" [[""     h/home]
        ["ping" h/ping]
        [true   h/not-found]]])

(def handler (make-handler routes))

(defn start-app
  []
  (reset! server (r/run-jetty handler {:host  "localhost"
                                       :port  (:ring-server-port @c/config)
                                       :join? false})))

(defn stop-app
  []
  (when @server (.stop @server))
  (reset! server nil))

(defn restart-app
  []
  (stop-app)
  (start-app))
