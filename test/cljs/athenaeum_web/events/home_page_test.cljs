(ns athenaeum-web.events.home-page-test
  (:require [cljs.test :refer-macros [deftest testing is run-tests]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [athenaeum-web.test-utils :as tu]
            [athenaeum-web.home-page.events :as home-page-events]
            [athenaeum-web.home-page.subscriptions :as home-page-subs]))

(deftest fetch-books-test
  (testing "On successfully fetching books, they should be put into db"
    (rf-test/run-test-sync
     (let [books [{:id 1 :title "book1" :author "author1"}
                  {:id 2 :title "book2" :author "author2"}]]
       (tu/stub-api-call books true)
       (rf/dispatch [::home-page-events/fetch-books])
       (is (= books @(rf/subscribe [::home-page-subs/books])))))))
