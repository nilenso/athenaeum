(ns athenaeum.middleware
  (:require [clojure.walk :as walk]
            [ring.util.response :as response]
            [athenaeum.session :as session]))

(defn wrap-exception-handling
  [handler]
  (fn [request]
    (try (handler request)
         (catch Exception _
           (-> (response/response {:message "Internal server error"})
               (response/status 500))))))

(defn wrap-require-session
  [handler]
  (fn [request]
    (let [request (update request :cookies walk/keywordize-keys)
          session-id (get-in request [:cookies :session-id :value])]
      (if session-id
        (if (session/fetch session-id)
          (handler request)
          (-> (response/response {:message "session does not exist. login and retry."})
              (response/status 401)))
        (response/bad-request {:message "session id cookie missing"})))))
