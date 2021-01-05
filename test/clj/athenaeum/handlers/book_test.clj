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
  (testing "Returns empty list when db is empty"
    (let [res (book/fetch {})]
      (is (= 200 (:status res)))
      (is (= [] (:body res)))))

  (testing "Returns list of books when db is non-empty"
    (tu/clear-tables)
    (let [created-book (db/with-transaction [tx @db/datasource]
                         (domain-book/create tx "test-title" "test-author"))
          res (book/fetch {})]
      (is (= 200 (:status res)))
      (is (= [created-book] (:body res))))))
