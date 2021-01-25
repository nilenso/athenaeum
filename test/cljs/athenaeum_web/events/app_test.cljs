(ns athenaeum-web.events.app-test
  (:require [cljs.test :refer-macros [deftest testing is run-tests]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [athenaeum-web.app.events.core :as events]
            [athenaeum-web.app.subscriptions :as subs]
            [athenaeum-web.test-utils :as tu]))

(deftest initialize-db-test
  (testing "When db is initialized, db is reset to default state"
    (rf-test/run-test-sync
     (rf/dispatch [::events/initialize-db])
     (is (= :logged-out @(rf/subscribe [::subs/login-state])))
     (is (= :home-page (:handler @(rf/subscribe [::subs/current-page]))))
     (is (= false @(rf/subscribe [::subs/auth2-loaded]))))))

(deftest auth2-loaded-status-test
  (testing "When auth2 library is loaded, :auth2-loaded in db is set to true"
    (rf-test/run-test-sync
     (rf/dispatch [::events/auth2-loaded-status true])
     (is (= true @(rf/subscribe [::subs/auth2-loaded])))))

  (testing "When auth2 library is not loaded, :auth2-loaded in db is set to false"
    (rf-test/run-test-sync
     (rf/dispatch [::events/auth2-loaded-status false])
     (is (= false @(rf/subscribe [::subs/auth2-loaded]))))))

(deftest fetch-user-test
  (testing "On successfully fetching user, user is added to db and login state is set to logged-in"
    (rf-test/run-test-sync
     (let [user {:id 1 :name "name"}]
       (tu/initialize-db)
       (tu/stub-api-call {:user user} true)
       (rf/dispatch [::events/fetch-user])
       (is (= user @(rf/subscribe [::subs/user])))
       (is (= :logged-in @(rf/subscribe [::subs/login-state]))))))

  (testing "On successfully fetching user and given a non-nil callback, the callback is also dispatched"
    (rf-test/run-test-sync
     (let [user {:id 1 :name "name"}
           callback-event (tu/stub-event ::call-on-success)]
       (tu/initialize-db)
       (tu/stub-api-call {:user user} true)
       (rf/dispatch [::events/fetch-user ::call-on-success nil])
       (is (= user @(rf/subscribe [::subs/user])))
       (is (= :logged-in @(rf/subscribe [::subs/login-state])))
       (is (= [::call-on-success] @callback-event)))))

  (testing "On failing to fetch user, login state is set to logged-out"
    (rf-test/run-test-sync
     (tu/initialize-db)
     (tu/stub-api-call {} false)
     (rf/dispatch [::events/fetch-user])
     (is (= :logged-out @(rf/subscribe [::subs/login-state])))))

  (testing "On failing to fetch user and given a non-nil callback, the callback is also dispatched"
    (rf-test/run-test-sync
     (let [callback-event (tu/stub-event ::call-on-failure)]
       (tu/initialize-db)
       (tu/stub-api-call {} false)
       (rf/dispatch [::events/fetch-user ::foo ::call-on-failure])
       (is (= :logged-out @(rf/subscribe [::subs/login-state])))
       (is (= [::call-on-failure] @callback-event))))))
