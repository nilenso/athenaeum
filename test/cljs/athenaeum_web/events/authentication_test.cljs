(ns athenaeum-web.events.authentication-test
  (:require [cljs.test :refer-macros [deftest testing is run-tests]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [re-frame.db :as rf-db]
            [athenaeum-web.test-utils :as tu]
            [athenaeum-web.app.events.authentication :as authentication-events]
            [athenaeum-web.app.events.core :as app-events]))

(deftest authentication-check-test
  (testing "If user is logged in, dispatch the on-success event"
    (rf-test/run-test-sync
     (let [on-success-event ::test
           stubbed-event (tu/stub-event on-success-event)]
       (tu/initialize-db)
       (swap! rf-db/app-db assoc :login-state :logged-in)
       (rf/dispatch [::authentication-events/authentication-check on-success-event])
       (is (= [on-success-event] @stubbed-event)))))

  (testing "If user is logged out, dispatch fetch-user event with on-success and redirect-to-login events as params"
    (rf-test/run-test-sync
     (let [on-success-event ::test
           stubbed-event (tu/stub-event ::app-events/fetch-user)]
       (tu/initialize-db)
       (swap! rf-db/app-db assoc :login-state :logged-out)
       (rf/dispatch [::authentication-events/authentication-check on-success-event])
       (is (= ::app-events/fetch-user (first @stubbed-event)))
       (is (= [::test ::authentication-events/redirect-to-login] (next @stubbed-event)))))))
