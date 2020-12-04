(ns athenaeum.handlers
  (:require [ring.util.response :as response]))

(defn ping
  [_]
  (response/response "pong"))

(defn index
  [_]
  (-> (response/resource-response "public/index.html")
      (response/content-type "text/html")))
