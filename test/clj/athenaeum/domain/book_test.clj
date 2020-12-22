(ns athenaeum.domain.book-test
  (:require [clojure.test :refer :all]
            [next.jdbc :as jdbc]
            [athenaeum.fixtures :as fixtures]
            [athenaeum.domain.book :as book]
            [athenaeum.db :as db]))

(use-fixtures :once fixtures/load-config fixtures/set-datasource)
(use-fixtures :each fixtures/clear-tables)

(deftest fetch-all
  (testing "Returns empty list when db is empty"
    (let [res (jdbc/with-transaction [tx @db/datasource]
                (book/fetch-all tx))]
      (is (= [] res))))

  (testing "Returns list of books when db is non-empty"
    (let [_book1 (jdbc/with-transaction [tx @db/datasource]
                  (book/create tx "book1" "author1"))
          _book2 (jdbc/with-transaction [tx @db/datasource]
                  (book/create tx "book2" "author2"))
          _book3 (jdbc/with-transaction [tx @db/datasource]
                  (book/create tx "book3" "author3"))
          res (jdbc/with-transaction [tx @db/datasource]
                (book/fetch-all tx))]
      (is (= 3 (count res)))
      (is (= "book1" (:books/title (first res))))
      (is (= "author2" (:books/author (second res))))
      (is (= #:books{:title "book3", :author "author3", :isbn nil, :year_of_publication nil}
             (dissoc (last res) :books/id))))))
