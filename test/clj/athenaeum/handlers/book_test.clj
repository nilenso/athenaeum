(ns athenaeum.handlers.book-test
  (:require [clojure.test :refer :all]
            [athenaeum.fixtures :as fixtures]
            [athenaeum.handlers.book :as book]
            [athenaeum.domain.book :as domain-book]
            [athenaeum.db :as db]
            [athenaeum.test-utils :as tu]
            [athenaeum.session.core :as session]
            [athenaeum.session.redis :as redis]))

(use-fixtures :once fixtures/load-config fixtures/set-datasource)
(use-fixtures :each fixtures/clear-tables)

(deftest fetch-test
  (testing "Returns empty list when db is empty"
    (let [session-id (session/create-and-get-id {})
          req {:cookies {"session-id" session-id}}
          res (book/fetch req)]
      (redis/delete-key session-id)
      (is (= 200 (:status res)))
      (is (= [] (:body res)))))

  (testing "Returns list of books when db is non-empty"
    (tu/clear-tables)
    (let [created-book (db/with-transaction [tx @db/datasource]
                         (domain-book/create tx "test-title" "test-author"))
          session-id (session/create-and-get-id {})
          req {:cookies {"session-id" session-id}}
          res (book/fetch req)]
      (redis/delete-key session-id)
      (is (= 200 (:status res)))
      (is (= [created-book] (:body res)))))

  (testing "If cookie contains invalid session id, respond with status 401"
    (tu/clear-tables)
    (redis/delete-key "invalid-session-id")
    (let [req {:cookies {"session-id" "invalid-session-id"}}
          res (book/fetch req)]
      (is (= 401 (:status res))))))
