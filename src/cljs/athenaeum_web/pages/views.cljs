(ns athenaeum-web.pages.views
  (:require [re-frame.core :as rf]
            [athenaeum-web.subscriptions :as s]
            [athenaeum-web.routes :as r]))

(defn- page-link
  [handler page-name]
  [:a {:class (str "nav-item nav-link pr-3"
                   (when
                    (= handler
                       (:handler @(rf/subscribe [::s/current-page])))
                     " active"))
       :href  (r/path-for handler)} page-name])

(defn navbar
  []
  [:nav.navbar.navbar-light.bg-light.flex-row
   [:a.navbar-brand.float-left {:href (r/path-for :home-page)} "Athenaeum"]
   [:div.navbar-nav.flex-row.float-right
    [page-link :home-page "Home"]]])
