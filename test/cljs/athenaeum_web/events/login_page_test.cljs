(ns athenaeum-web.events.login-page-test
  (:require [cljs.test :refer-macros [deftest testing is run-tests]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [re-frame.db :as rf-db]
            [athenaeum-web.test-utils :as tu]
            [athenaeum-web.login-page.events :as login-page-events]
            [athenaeum-web.app.subscriptions :as subs]
            [athenaeum-web.app.effects :as effects]))

(deftest login-test
  (testing "On successful login, login state should be set to logged-in and set home page"
    (rf-test/run-test-sync
     (let [navigate-to-params (tu/stub-effect ::effects/navigate-to)]
       (tu/initialize-db)
       (tu/stub-api-call {} true)
       (rf/dispatch [::login-page-events/login "test-token"])
       (is (= :logged-in @(rf/subscribe [::subs/login-state])))
       (is (= ["/"] @navigate-to-params)))))

  (testing "On failing to login, login state should be logged-out"
    (rf-test/run-test-sync
     (tu/initialize-db)
     (tu/stub-api-call {} false)
     (rf/dispatch [::login-page-events/login "test-token"])
     (is (= :logged-out @(rf/subscribe [::subs/login-state]))))))

(deftest login-page-navigated-test
  (testing "When login page is navigated and user is logged in, home page is set"
    (rf-test/run-test-sync
     (let [navigate-to-params (tu/stub-effect ::effects/navigate-to)]
       (tu/initialize-db)
       (swap! rf-db/app-db assoc :login-state :logged-in)
       (rf/dispatch [::login-page-events/login-page-navigated])
       (is (= ["/"] @navigate-to-params))))))
