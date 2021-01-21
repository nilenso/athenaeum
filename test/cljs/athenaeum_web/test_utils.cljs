(ns athenaeum-web.test-utils
  (:require [re-frame.core :as rf]
            [athenaeum-web.app.db :as db]
            [re-frame.db :as rf-db]))

(defn initialize-db
  []
  (reset! rf-db/app-db db/default-db))

(defn stub-api-call
  [response success?]
  (rf/reg-fx
   :http-xhrio
   (fn [{:keys [on-success on-failure]}]
     (if success?
       (rf/dispatch (conj on-success response))
       (rf/dispatch (conj on-failure response))))))

(defn stub-event
  [event-name]
  (let [dispatched-event (atom nil)]
    (rf/reg-event-fx
     event-name
     (fn [_ event]
       (reset! dispatched-event event)
       {}))
    dispatched-event))

(defn stub-effect
  [effect-name]
  (let [params (atom nil)]
    (rf/reg-fx
     effect-name
     (fn [& args]
       (reset! params args)
       nil))
    params))
