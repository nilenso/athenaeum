(ns athenaeum-web.events.page-test
  (:require [cljs.test :refer-macros [deftest testing is run-tests]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [re-frame.db :as rf-db]
            [athenaeum-web.test-utils :as tu]
            [athenaeum-web.page.events :as page-events]
            [athenaeum-web.app.effects :as effects]
            [athenaeum-web.app.subscriptions :as subs]))

(deftest logout-test
  (testing "On successful logout, login state should be set to logged-out and login page should be navigated to"
    (rf-test/run-test-sync
     (let [navigate-to-params (tu/stub-effect ::effects/navigate-to)]
       (tu/initialize-db)
       (tu/stub-api-call {} true)
       (rf/dispatch [::page-events/logout])
       (is (= :logged-out @(rf/subscribe [::subs/login-state])))
       (is (= ["/login"] @navigate-to-params)))))

  (testing "On failed logout, db is unchanged"
    (rf-test/run-test-sync
     (tu/initialize-db)
     (let [db-before @rf-db/app-db]
       (tu/stub-api-call {} false)
       (rf/dispatch [::page-events/logout])
       (is (= @rf-db/app-db db-before))))))
