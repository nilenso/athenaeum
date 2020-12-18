(ns athenaeum.server
  (:require [ring.adapter.jetty :as jetty]
            [athenaeum.handlers :as handlers]
            [athenaeum.config :as config]
            [bidi.ring :refer (make-handler)]
            [ring.middleware.resource :refer [wrap-resource]]
            [athenaeum.books.handlers :as books-handlers]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-response]]))

(defonce server (atom nil))

(def routes
  ["/" [["api/" {"books" {"" {:get  books-handlers/fetch
                              :post books-handlers/create}}}]
        [true handlers/index]]])

(def handler
  (-> routes
      make-handler
      (wrap-json-response)
      (wrap-keyword-params)
      (wrap-params)
      (wrap-resource "public")))

(defn start-app
  []
  (reset! server (jetty/run-jetty handler {:host  "localhost"
                                           :port  (:ring-server-port @config/config)
                                           :join? false})))

(defn stop-app
  []
  (when @server (.stop @server))
  (reset! server nil))

(defn restart-app
  []
  (stop-app)
  (start-app))
