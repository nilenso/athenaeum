(ns athenaeum.middleware-test
  (:require [clojure.test :refer :all]
            [athenaeum.fixtures :as fixtures]
            [athenaeum.middleware :as middleware]
            [athenaeum.test-utils :as tu]
            [athenaeum.session :as session]))

(use-fixtures :once fixtures/load-config fixtures/set-redis-server-conn)

(deftest wrap-exception-handling-test
  (testing "Calls handler in a try block and responds with status 500 if it catches any exceptions"
    (let [rq {}
          handler (fn [_] (throw Exception))]
      (is (= 500 (-> rq
                     ((middleware/wrap-exception-handling handler))
                     (:status))))))

  (testing "Simply calls handler if no exception is thrown"
    (let [rq {}
          handler (constantly 1)]
      (is (= 1 ((middleware/wrap-exception-handling handler) rq))))))

(deftest wrap-require-session-test
  (testing "If session id cookie and the corresponding session exists, calls wrapped handler"
    (tu/with-fixtures
      [fixtures/clear-sessions]
      (let [session-id (session/create 1)
            rq {:cookies {"session-id" {"value" session-id}}}
            handler (constantly 1)]
        (is (= 1 ((middleware/wrap-require-session handler) rq))))))

  (testing "If session does not exist, returns status 401"
    (tu/with-fixtures
      [fixtures/clear-sessions]
      (let [rq {:cookies {"session-id" {"value" "invalid-session-id"}}}
            handler (constantly 1)]
        (is (= {:status  401
                :headers {}
                :body    {:message "session does not exist. login and retry."}}
               ((middleware/wrap-require-session handler) rq))))))

  (testing "If session id cookie does not exist, returns status 400"
    (let [rq {}
          handler (constantly 1)]
      (is (= {:status  400
              :headers {}
              :body    {:message "session id cookie missing"}}
             ((middleware/wrap-require-session handler) rq))))))
