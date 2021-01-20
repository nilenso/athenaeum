(ns athenaeum-web.app.effects
  (:require [re-frame.core :as rf]
            [pushy.core :as p]
            [athenaeum-web.routes :as r]))

(rf/reg-fx
 :navigate-to
 (fn [token]
   (p/replace-token! @r/history token)))
