(ns athenaeum.handlers
  (:require [ring.util.response :as response]))

(defn index
  [_]
  (-> (response/resource-response "public/index.html")
      (response/content-type "text/html")))
