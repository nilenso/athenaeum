(ns athenaeum.handlers.book-test
  (:require [clojure.test :refer :all]
            [athenaeum.fixtures :as fixtures]
            [athenaeum.handlers.book :as book]
            [athenaeum.domain.book :as domain-book]
            [athenaeum.db :as db]
            [athenaeum.session :as session]
            [athenaeum.redis :as redis]
            [athenaeum.test-utils :as tu]))

(use-fixtures :once fixtures/load-config fixtures/set-datasource fixtures/set-redis-server-conn)
(use-fixtures :each fixtures/clear-tables fixtures/clear-redis)

(deftest fetch-test
  (testing "Returns empty list when db is empty"
    (let [session-id (session/create-and-return-id {})
          req {:cookies {"session-id" {:value session-id}}}
          res (book/fetch req)]
      (is (= 200 (:status res)))
      (is (= [] (:body res)))))

  (testing "Returns list of books when db is non-empty"
    (tu/with-fixtures
      [fixtures/clear-tables fixtures/clear-redis]
      (let [created-book (db/with-transaction [tx @db/datasource]
                           (domain-book/create tx "test-title" "test-author"))
            session-id (session/create-and-return-id {})
            req {:cookies {"session-id" {:value session-id}}}
            res (book/fetch req)]
        (redis/delete-key session-id)
        (is (= 200 (:status res)))
        (is (= [created-book] (:body res))))))

  (testing "If cookie contains invalid session id, respond with status 401"
    (let [req {:cookies {"session-id" {:value "invalid-session-id"}}}
          res (book/fetch req)]
      (is (= 401 (:status res))))))
