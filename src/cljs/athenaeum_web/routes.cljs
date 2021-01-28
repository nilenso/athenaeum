(ns athenaeum-web.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as rf]
            [athenaeum-web.app.events :as events]))

(def routes
  ["/" [["" :home-page]
        ["login" :login-page]
        [true :not-found]]])

;; Pushy functions
(defonce history (atom nil))

(defn set-page
  [page]
  (rf/dispatch [::set-current-page page]))

(defn path-for
  [handler]
  (bidi/path-for routes handler))

(defn init
  []
  (reset! history (pushy/pushy set-page (partial bidi/match-route routes)))
  (pushy/start! @history))

;; Effect handler
(rf/reg-fx
 ::navigate-to
 (fn [token]
   (pushy/set-token! @history token)))

;; Multi-method called on route change
(defmulti on-route-change-event :handler :default ::default)

(defmethod on-route-change-event
  ::default
  [_]
  nil)

;; Set page
(def authenticated-pages
  #{:home-page})

(defn- authenticated-page-event
  [login-state authenticated-event]
  (if (= login-state :logged-in)
    [authenticated-event]
    [::events/fetch-user authenticated-event ::redirect-to-login]))

(defn- page-event
  [route login-state]
  (if (contains? authenticated-pages (:handler route))
    (authenticated-page-event login-state (on-route-change-event route))
    [(on-route-change-event route)]))

(rf/reg-event-fx
 ::set-current-page
 (fn [{:keys [db]} [_ route]]
   {:db (assoc db :page route)
    :fx (let [event (page-event route (:login-state db))]
          (if (first event) [[:dispatch event]] []))}))

(rf/reg-event-fx
 ::redirect-to-login
 (fn [_ _]
   {:fx [[::navigate-to (path-for :login-page)]]}))
