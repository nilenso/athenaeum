(ns athenaeum.domain.book-test
  (:require [clojure.test :refer :all]
            [athenaeum.fixtures :as fixtures]
            [athenaeum.domain.book :as book]
            [athenaeum.db :as db]
            [athenaeum.test-utils :as tu]))

(use-fixtures :once fixtures/load-config fixtures/set-datasource)
(use-fixtures :each fixtures/clear-tables)

(deftest fetch-all
  (testing "Returns empty list when db is empty"
    (let [res (db/with-transaction [tx @db/datasource]
                (book/fetch-all tx))]
      (is (= [] res))))

  (testing "Returns list of books when db is non-empty"
    (tu/with-fixtures
      [fixtures/clear-tables]
      (let [book1 (db/with-transaction [tx @db/datasource]
                    (book/create tx "book1" "author1"))
            book2 (db/with-transaction [tx @db/datasource]
                    (book/create tx "book2" "author2"))
            books [book1 book2]
            returned-books (db/with-transaction [tx @db/datasource]
                             (book/fetch-all tx))]
        (is (= books returned-books))))))
