(ns athenaeum.server
  (:require [ring.adapter.jetty :as r]
            [athenaeum.handlers :as h]
            [athenaeum.config :as c]
            [bidi.ring :refer (make-handler)]
            [ring.middleware.resource :refer [wrap-resource]]
            [athenaeum.books.handlers :as bh]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]))

(defonce server (atom nil))

(def routes
  ["/" [["api/" {"books" {"" {:get  #'bh/fetch
                              :post #'bh/create}}}]
        [true h/index]]])

(def handler
  (-> routes
      make-handler
      (wrap-keyword-params)
      (wrap-params)
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
