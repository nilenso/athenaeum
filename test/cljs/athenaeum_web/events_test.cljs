(ns athenaeum-web.events-test
  (:require [cljs.test :refer-macros [deftest testing is run-tests]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [athenaeum-web.app.events :as events]
            [athenaeum-web.home-page.events :as home-page-events]
            [athenaeum-web.app.subscriptions :as subs]
            [athenaeum-web.home-page.subscriptions :as home-page-subs]
            [athenaeum-web.test-utils :as tu]))

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

(deftest fetch-books-test
  (testing "On successfully fetching books, they should be put into db"
    (rf-test/run-test-sync
     (let [books [{:id 1 :title "book1" :author "author1"}
                  {:id 2 :title "book2" :author "author2"}]]
       (tu/stub-api-call books true)
       (rf/dispatch [::home-page-events/fetch-books])
       (is (= books @(rf/subscribe [::home-page-subs/books])))))))
