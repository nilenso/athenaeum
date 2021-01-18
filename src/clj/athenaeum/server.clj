(ns athenaeum.server
  (:require [ring.adapter.jetty :as jetty]
            [athenaeum.handlers.core :as html]
            [athenaeum.config :as config]
            [athenaeum.handlers.book :as book]
            [athenaeum.handlers.user :as user]
            [bidi.ring :refer (make-handler)]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.session.cookie]))

(defonce server (atom nil))

(def routes
  ["/" [["api/" [["books" {:get book/fetch}]
                 ["login" {:post user/login}]
                 ["logout" {:get user/logout}]]]
        [true html/index]]])

(def handler
  (-> routes
      make-handler
      (wrap-json-response)
      (wrap-json-body)
      (wrap-keyword-params)
      (wrap-params)
      (wrap-cookies)
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
