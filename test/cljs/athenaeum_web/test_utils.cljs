(ns athenaeum-web.test-utils
  (:require [re-frame.core :as rf]))

(defn stub-api-call
  [response success?]
  (rf/reg-fx
   :http-xhrio
   (fn [{:keys [on-success on-failure]}]
     (if success?
       (rf/dispatch (conj on-success response))
       (rf/dispatch (conj on-failure response))))))
