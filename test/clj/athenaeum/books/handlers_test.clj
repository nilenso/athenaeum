(ns athenaeum.books.handlers-test
  (:require [clojure.test :refer :all]
            [next.jdbc :as jdbc]
            [athenaeum.fixtures :as f]
            [athenaeum.books.handlers :as books]
            [athenaeum.books.db :as books-db]
            [athenaeum.db :as db]))

(use-fixtures :once f/config-and-datasource)
(use-fixtures :each f/clear-tables)

(deftest create-test
  (testing "Adds book given its name and author"
    (let [req {:params {:name   "test-book"
                        :author "test-author"}}
          res (books/create req)]
      (is (= 200 (:status res)))
      (is (= #:books{:name "test-book", :author "test-author", :isbn nil, :year_of_publication nil}
             (dissoc (:body res) :books/id))))))

(deftest fetch-test
  (testing "Returns empty list if db is empty"
    (let [res (books/fetch {})]
      (is (= 200 (:status res)))
      (is (= [] (:body res)))))
  (testing "Returns list of all books if any in db"
    (let [created-book (jdbc/with-transaction [tx @db/datasource]
                         (books-db/create tx "test-book" "test-author"))
          res (books/fetch {})]
      (is (= 200 (:status res)))
      (is (= [created-book] (:body res))))))
