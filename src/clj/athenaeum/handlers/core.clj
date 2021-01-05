(ns athenaeum.handlers.core
  (:require [ring.util.response :as response]))

(defn index
  [_]
  (-> (response/resource-response "public/index.html")
      (response/content-type "text/html")))
