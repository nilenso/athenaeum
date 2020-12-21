(ns athenaeum.handlers.book-test
  (:require [clojure.test :refer :all]
            [next.jdbc :as jdbc]
            [athenaeum.fixtures :as fixtures]
            [athenaeum.handlers.book :as book]
            [athenaeum.domain.book :as domain-book]
            [athenaeum.db :as db]))

(use-fixtures :once fixtures/load-config fixtures/set-datasource)
(use-fixtures :each fixtures/clear-tables)

(deftest fetch-test
  (testing "Returns empty list if db is empty"
    (let [res (book/fetch {})]
      (is (= 200 (:status res)))
      (is (= [] (:body res)))))
  (testing "Returns list of all books if any in db"
    (let [created-book (jdbc/with-transaction [tx @db/datasource]
                         (domain-book/create tx "test-title" "test-author"))
          res (book/fetch {})]
      (is (= 200 (:status res)))
      (is (= [created-book] (:body res))))))
