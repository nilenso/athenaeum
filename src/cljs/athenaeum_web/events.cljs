(ns athenaeum-web.events
  (:require [re-frame.core :as rf]))

(rf/reg-event-db
 ::set-current-page
 (fn [db [_ current-page]]
   (assoc db :page current-page)))
