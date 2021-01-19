(ns athenaeum-web.events.login-page-test
  (:require [cljs.test :refer-macros [deftest testing is run-tests]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [athenaeum-web.test-utils :as tu]
            [athenaeum-web.login-page.events :as login-page-events]
            [athenaeum-web.app.subscriptions :as subs]))

(deftest login-test
  (testing "On successfully verifying id token, login state should be logged in"
    (rf-test/run-test-sync
     (tu/stub-api-call {} true)
     (rf/dispatch [::login-page-events/login "test-token"])
     (is (= :logged-in @(rf/subscribe [::subs/login-state])))))

  (testing "On failing to verify id token, login state should be logged out"
    (rf-test/run-test-sync
     (tu/stub-api-call {} false)
     (rf/dispatch [::login-page-events/login "test-token"])
     (is (= :logged-out @(rf/subscribe [::subs/login-state]))))))
