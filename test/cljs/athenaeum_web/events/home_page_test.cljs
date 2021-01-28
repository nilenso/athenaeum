(ns athenaeum-web.events.home-page-test
  (:require [cljs.test :refer-macros [deftest testing is run-tests]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [athenaeum-web.test-utils :as tu]
            [athenaeum-web.home-page.events :as home-page-events]
            [athenaeum-web.home-page.subscriptions :as home-page-subs]
            [athenaeum-web.app.subscriptions]))

(deftest home-page-navigated-test
  (testing "When home page is navigated, fetch-books is dispatched"
    (rf-test/run-test-sync
     (let [stubbed-event (tu/stub-event ::home-page-events/fetch-books)]
       (rf/dispatch [::home-page-events/home-page-navigated])
       (is (= [::home-page-events/fetch-books] @stubbed-event))))))

(deftest fetch-books-test
  (testing "On successfully fetching books, they should be put into db"
    (rf-test/run-test-sync
     (let [books [{:id 1 :title "book1" :author "author1"}
                  {:id 2 :title "book2" :author "author2"}]]
       (tu/initialize-db)
       (tu/stub-api-call books true)
       (rf/dispatch [::home-page-events/fetch-books])
       (is (= books @(rf/subscribe [::home-page-subs/books]))))))

  (testing "On failing to fetch books, there are no books in the db"
    (rf-test/run-test-sync
     (tu/initialize-db)
     (tu/stub-api-call {} false)
     (rf/dispatch [::home-page-events/fetch-books])
     (is (= [] @(rf/subscribe [::home-page-subs/books]))))))
