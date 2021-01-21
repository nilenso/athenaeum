(ns athenaeum.middleware
  (:require [clojure.walk :as walk]
            [ring.util.response :as response]))

(defn wrap-require-session-id-cookie*
  [handler]
  (fn [{:keys [cookies] :as request}]
    (if (get-in cookies [:session-id :value])
      (handler request)
      (response/bad-request {:message "session id cookie missing"}))))

(defn wrap-require-id-token-header*
  [handler]
  (fn [{:keys [headers] :as request}]
    (if (:id-token headers)
      (handler request)
      (response/bad-request {:message "id token header missing"}))))

(defn wrap-keywordize-cookies-and-headers
  [f handler]
  (fn [{:keys [headers cookies] :as request}]
    (let [headers (walk/keywordize-keys headers)
          cookies (walk/keywordize-keys cookies)]
      ((f handler) (-> request
                       (assoc :headers headers)
                       (assoc :cookies cookies))))))

(def wrap-require-id-token-header
  (fn [handler]
    (wrap-keywordize-cookies-and-headers wrap-require-id-token-header* handler)))

(def wrap-require-session-id-cookie
  (fn [handler]
    (wrap-keywordize-cookies-and-headers wrap-require-session-id-cookie* handler)))
