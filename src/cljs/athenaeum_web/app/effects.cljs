(ns athenaeum-web.app.effects
  (:require [re-frame.core :as rf]
            [pushy.core :as p]
            [athenaeum-web.routes :as r]))

(rf/reg-fx
 :history-token
 (fn [token]
   (p/set-token! @r/history token)))