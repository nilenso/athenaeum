(ns clj.athenaeum.handlers
  (:require [ring.util.response :as response]))

(defn home
  [_]
  (response/response "hi world"))

(defn ping
  [_]
  (response/response "pong"))

(defn not-found
  [_]
  (response/not-found "Page not found."))
