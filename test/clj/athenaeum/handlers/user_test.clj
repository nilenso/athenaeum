(ns athenaeum.handlers.user-test
  (:require [clojure.test :refer :all]
            [athenaeum.fixtures :as fixtures]
            [athenaeum.handlers.user :as user]))

(use-fixtures :once fixtures/load-config fixtures/set-datasource)
(use-fixtures :each fixtures/clear-tables)

(deftest login-test
  (testing "Returns status 200 on successful id token verification"
    (let [req {:headers {:id-token "valid-id-token"}}
          res (with-redefs [user/verify-id-token (constantly true)
                            user/confirm-login (constantly true)]
                (user/login req))]
      (is (= 200 (:status res)))
      (is (= "login success" (get-in res [:body :text])))))

  (testing "Returns status 400 if invalid id token is passed"
    (let [req {:headers {:id-token "invalid-id-token"}}
          res (user/login req)]
      (is (= 400 (:status res)))
      (is (= "login failed" (get-in res [:body :text])))))

  (testing "Returns status 400 if id-token header is missing"
    (let [req {}
          res (user/login req)]
      (is (= 400 (:status res)))
      (is (= "Id token header missing" (get-in res [:body :text]))))))
