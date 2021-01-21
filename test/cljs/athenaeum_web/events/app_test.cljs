(ns athenaeum-web.events.app-test
  (:require [cljs.test :refer-macros [deftest testing is run-tests]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [athenaeum-web.app.events :as events]
            [athenaeum-web.app.subscriptions :as subs]
            [athenaeum-web.test-utils :as tu]))

(deftest initialize-db-test
  (testing "When db is initialized, login state is initialized to logged-out and user is fetched"
    (rf-test/run-test-sync
     (let [fetch-user-event (tu/stub-event ::events/fetch-user)]
       (rf/dispatch [::events/initialize-db])
       (is (= :logged-out @(rf/subscribe [::subs/login-state])))
       (is (= [::events/fetch-user] @fetch-user-event))))))

(deftest set-current-page-test
  (testing "When dispatched, current page is set in db"
    (rf-test/run-test-sync
     (rf/dispatch [::events/set-current-page {:handler :test}])
     (is (= {:handler :test} @(rf/subscribe [::subs/current-page]))))))

(deftest fetch-user-test
  (testing "On successfully fetching user, user is added to db and login state is set to logged-in"
    (rf-test/run-test-sync
     (let [user {:id 1 :name "name"}]
       (tu/initialize-db)
       (tu/stub-api-call {:user user} true)
       (rf/dispatch [::events/fetch-user])
       (is (= user @(rf/subscribe [::subs/user])))
       (is (= :logged-in @(rf/subscribe [::subs/login-state]))))))

  (testing "On failing to fetch user, login state is set to logged-out"
    (rf-test/run-test-sync
     (tu/initialize-db)
     (tu/stub-api-call {} false)
     (rf/dispatch [::events/fetch-user])
     (is (= :logged-out @(rf/subscribe [::subs/login-state]))))))
