(ns clj.athenaeum.server
  (:require [ring.adapter.jetty :as r]
            [clj.athenaeum.handlers :as h]
            [clj.athenaeum.config :as c]))

(defonce server (atom nil))

(defn start-app
  []
  (reset! server (r/run-jetty h/app-handler {:host  "localhost"
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
