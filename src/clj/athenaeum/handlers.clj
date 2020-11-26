(ns clj.athenaeum.handlers
  (:require [compojure.core :as c]
            [compojure.route :as r]))

(defn home-handler
  [_]
  "hi world")

(defn ping-handler
  [_]
  "pong")

(c/defroutes app-handler
  (c/GET "/" _ home-handler)
  (c/GET "/ping" _ ping-handler)
  (r/not-found "page not found"))
