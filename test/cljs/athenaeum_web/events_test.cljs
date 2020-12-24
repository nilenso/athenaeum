(ns athenaeum-web.events-test
  (:require [cljs.test :refer-macros [deftest testing is run-tests]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [athenaeum-web.events :as e]
            [athenaeum-web.subscriptions :as s]
            [athenaeum-web.test-utils :as tu]))

(deftest initialize-db-test
  (testing "When db is initialized, current page should be home-page"
    (rf-test/run-test-sync
     (rf/dispatch [::e/initialize-db])
     (is (= {:handler :home-page} @(rf/subscribe [::s/current-page]))))))

(deftest set-current-page-test
  (testing "When set-current-page event is dispatched, current page is in db"
    (rf-test/run-test-sync
     (rf/dispatch [::e/initialize-db])
     (rf/dispatch [::e/set-current-page {:handler :test}])
     (is (= {:handler :test} @(rf/subscribe [::s/current-page]))))))

(deftest fetch-books-test
  (testing "On successfully fetching books, they should be put into db"
    (rf-test/run-test-sync
     (let [books [{:id 1 :title "book1" :author "author1"}
                  {:id 2 :title "book2" :author "author2"}]]
       (tu/stub-api-call books true)
       (rf/dispatch [::e/fetch-books])
       (is (= books @(rf/subscribe [::s/books])))))))
