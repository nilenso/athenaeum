(ns athenaeum.books.handlers-test
  (:require [clojure.test :refer :all]
            [athenaeum.books.handlers :as books]
            [athenaeum.fixtures :as f]
            [athenaeum.db :as db]))

(use-fixtures :once f/config-and-datasource)
(use-fixtures :each f/clear-tables)

(deftest fetch-test
  (testing "Returns string of books"
    (let [res (books/fetch @db/datasource)]
      (is (= 200 (:status res)))
      (is (= "[]" (:body res))))))

(deftest create-test
  (testing "Adds book given its name and author"
    (let [req {:params {:name   "test-book"
                        :author "test-author"}}
          res (books/create req)]
      (is (= 200 (:status res)))
      (is (= "#:books{:id 2, :name \"test-book\", :author \"test-author\", :isbn nil, :year_of_publication nil}"
             (:body res))))))
