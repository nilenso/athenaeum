(ns athenaeum.handlers.book-test
  (:require [clojure.test :refer :all]
            [athenaeum.fixtures :as fixtures]
            [athenaeum.handlers.book :as book]
            [athenaeum.domain.book :as domain-book]
            [athenaeum.db :as db]
            [athenaeum.test-utils :as tu]))

(use-fixtures :once fixtures/load-config fixtures/set-datasource)
(use-fixtures :each fixtures/clear-tables)

(deftest fetch-test
  (testing "Returns list of available books"
    (let [created-book (db/with-transaction [tx @db/datasource]
                         (domain-book/create tx "test-title" "test-author"))
          req {}
          res (book/fetch req)]
      (is (= 200 (:status res)))
      (is (= [created-book] (:body res)))))

  (testing "When db is empty, returns empty list "
    (tu/with-fixtures
      [fixtures/clear-tables]
      (let [req {}
            res (book/fetch req)]
        (is (= 200 (:status res)))
        (is (= [] (:body res)))))))
