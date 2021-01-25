(ns athenaeum.middleware
  (:require [clojure.walk :as walk]
            [ring.util.response :as response]
            [athenaeum.session :as session]))

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

(defn wrap-exception-handling
  [handler]
  (fn [request]
    (try (handler request)
         (catch Exception _
           (-> (response/response {:message "Internal server error"})
               (response/status 500))))))

(defn wrap-require-session*
  [handler]
  (fn [{:keys [cookies] :as request}]
    (if (session/fetch (get-in cookies [:session-id :value]))
      (handler request)
      (-> (response/response {:message "session does not exist. login and retry."})
          (response/status 401)))))

(def wrap-require-session
  (comp wrap-require-session-id-cookie
        wrap-require-session*))
