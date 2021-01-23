(ns athenaeum-web.events.home-page-test
  (:require [cljs.test :refer-macros [deftest testing is run-tests]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [re-frame.db :as rf-db]
            [athenaeum-web.test-utils :as tu]
            [athenaeum-web.home-page.events :as home-page-events]
            [athenaeum-web.home-page.subscriptions :as home-page-subs]
            [athenaeum-web.app.subscriptions]
            [athenaeum-web.app.effects :as effects]))

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

(deftest home-page-navigated-test
  (testing "When home page is navigated and user is logged in, books are fetched"
    (rf-test/run-test-sync
     (let [fetch-books-event (tu/stub-event ::home-page-events/fetch-books)]
       (tu/initialize-db)
       (swap! rf-db/app-db assoc :login-state :logged-in)
       (rf/dispatch [::home-page-events/home-page-navigated])
       (is (= [::home-page-events/fetch-books] @fetch-books-event)))))

  (testing "When home page is navigated and user is logged out, user is fetched"
    (rf-test/run-test-sync
     (let [navigate-to-effect-params (tu/stub-effect ::effects/navigate-to)]
       (tu/initialize-db)
       (tu/stub-api-call {} false)
       (rf/dispatch [::home-page-events/home-page-navigated])
       (is (= ["/login"] @navigate-to-effect-params))))))
