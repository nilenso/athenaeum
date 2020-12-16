(ns athenaeum.server
  (:require [ring.adapter.jetty :as r]
            [athenaeum.handlers :as h]
            [athenaeum.config :as c]
            [bidi.ring :refer (make-handler)]
            [ring.middleware.resource :refer [wrap-resource]]))

(defonce server (atom nil))

(def routes
  ["/" [["api/" [["ping" h/ping]]]
        [true h/index]]])

(def handler
  (-> routes
      make-handler
      (wrap-resource "public")))

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
