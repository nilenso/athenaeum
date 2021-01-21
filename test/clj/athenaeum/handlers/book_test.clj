(ns athenaeum.handlers.book-test
  (:require [clojure.test :refer :all]
            [athenaeum.fixtures :as fixtures]
            [athenaeum.handlers.book :as book]
            [athenaeum.domain.book :as domain-book]
            [athenaeum.db :as db]
            [athenaeum.session :as session]
            [athenaeum.test-utils :as tu]))

(use-fixtures :once fixtures/load-config fixtures/set-datasource fixtures/set-redis-server-conn)
(use-fixtures :each fixtures/clear-tables fixtures/clear-sessions)

(deftest fetch-test
  (testing "When user is logged in, returns list of available books"
    (let [created-book (db/with-transaction [tx @db/datasource]
                         (domain-book/create tx "test-title" "test-author"))
          session-id (session/create "1")
          req {:cookies {:session-id {:value session-id}}}
          res (book/fetch req)]
      (is (= 200 (:status res)))
      (is (= [created-book] (:body res)))))

  (testing "When user is logged in and db is empty, returns empty list "
    (tu/with-fixtures
      [fixtures/clear-tables fixtures/clear-sessions]
      (let [session-id (session/create "1")
            req {:cookies {:session-id {:value session-id}}}
            res (book/fetch req)]
        (is (= 200 (:status res)))
        (is (= [] (:body res))))))

  (testing "When user is not logged in, returns status 401"
    (tu/with-fixtures
      [fixtures/clear-tables fixtures/clear-sessions]
      (let [req {:cookies {:session-id {:value "1"}}}
            res (book/fetch req)]
        (is (= 401 (:status res)))))))
