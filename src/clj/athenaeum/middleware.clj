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
  [handler]
  (fn [{:keys [headers cookies] :as request}]
    (let [headers (walk/keywordize-keys headers)
          cookies (walk/keywordize-keys cookies)]
      (handler (-> request
                   (assoc :headers headers)
                   (assoc :cookies cookies))))))

(def wrap-require-id-token-header
  (comp wrap-require-id-token-header*
        wrap-keywordize-cookies-and-headers))

(def wrap-require-session-id-cookie
  (comp wrap-require-session-id-cookie*
        wrap-keywordize-cookies-and-headers))
