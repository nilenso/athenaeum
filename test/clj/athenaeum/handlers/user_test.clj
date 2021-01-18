(ns athenaeum.handlers.user-test
  (:require [clojure.test :refer :all]
            [athenaeum.fixtures :as fixtures]
            [athenaeum.handlers.user :as user]))

(use-fixtures :once fixtures/load-config fixtures/set-datasource)
(use-fixtures :each fixtures/clear-tables)

(deftest login-test
  (testing "Returns status 200, adds id-token cookie to response on successful id token verification"
    (let [req {:headers {:id-token "valid-id-token"}}
          res (with-redefs [user/verify-id-token (constantly true)
                            user/get-session-id (constantly "new-session-id")]
                (user/login req))]
      (is (= 200 (:status res)))
      (is (= "new-session-id" (get-in res [:cookies "session-id" :value])))))

  (testing "Returns status 400 if invalid id token is passed"
    (let [req {:headers {:id-token "invalid-id-token"}}
          res (user/login req)]
      (is (= 400 (:status res)))
      (is (= "login failed" (get-in res [:body :message])))))

  (testing "Returns status 400 if id-token header is missing"
    (let [req {}
          res (user/login req)]
      (is (= 400 (:status res)))
      (is (= "id token header missing" (get-in res [:body :message]))))))
