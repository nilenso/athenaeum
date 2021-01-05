(ns athenaeum-web.pages.home.events
  (:require [re-frame.core :as rf]
            [athenaeum-web.events.book :as book]))

(rf/reg-event-fx
 ::home-page-navigated
 (fn [_ _]
   {:fx [[:dispatch [::book/fetch-books]]]}))
