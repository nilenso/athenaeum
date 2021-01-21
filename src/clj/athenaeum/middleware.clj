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
  (fn [request]
    (handler (-> request
                 (update :cookies walk/keywordize-keys)
                 (update :headers walk/keywordize-keys)))))

(def wrap-require-id-token-header
  (comp wrap-keywordize-cookies-and-headers
        wrap-require-id-token-header*))

(def wrap-require-session-id-cookie
  (comp wrap-keywordize-cookies-and-headers
        wrap-require-session-id-cookie*))
