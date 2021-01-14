(ns athenaeum-web.events.app-test
  (:require [cljs.test :refer-macros [deftest testing is run-tests]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [athenaeum-web.app.events :as events]
            [athenaeum-web.app.subscriptions :as subs]))

(deftest initialize-db-test
  (testing "When db is initialized, current login state should be logged out"
    (rf-test/run-test-sync
     (rf/dispatch [::events/initialize-db])
     (is (= :logged-out @(rf/subscribe [::subs/login-state]))))))

(deftest set-current-page-test
  (testing "When set-current-page event is dispatched, current page is in db"
    (rf-test/run-test-sync
     (rf/dispatch [::events/initialize-db])
     (rf/dispatch [::events/set-current-page {:handler :test}])
     (is (= {:handler :test} @(rf/subscribe [::subs/current-page]))))))
