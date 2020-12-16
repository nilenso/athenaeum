(ns athenaeum-web.events
  (:require [re-frame.core :as rf]
            [athenaeum-web.db :as db]))

(rf/reg-event-db
  ::set-current-page
  (fn [db [_ current-page]]
    (assoc db :page current-page)))
