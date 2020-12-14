(ns athenaeum-web.core
  (:require [reagent.dom :as rdom]
            [athenaeum-web.views :as v]
            [athenaeum-web.routes :as r]))

(defn ^:dev/after-load run []
  (r/init!)
  (rdom/render [v/root] (js/document.getElementById "root")))
