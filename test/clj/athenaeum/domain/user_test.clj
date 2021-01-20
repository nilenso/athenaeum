(ns athenaeum.domain.user-test
  (:require [clojure.test :refer :all]
            [athenaeum.fixtures :as fixtures]
            [athenaeum.db :as db]
            [athenaeum.domain.user :as user]))

(use-fixtures :once fixtures/load-config fixtures/set-datasource)
(use-fixtures :each fixtures/clear-tables)

(deftest fetch-by-google-id-test
  (testing "If record with given google id does not exist, return nil"
    (let [returned-user (db/with-transaction [tx @db/datasource]
                          (user/fetch-by-google-id tx "test-id"))]
      (is (= nil returned-user))))
  (testing "If record with given google id exists, return it"
    (let [google-id "test-id"
          user (db/with-transaction [tx @db/datasource]
                 (user/create tx {:google-id google-id
                                  :name      "name"
                                  :email     "email"}))
          returned-user (db/with-transaction [tx @db/datasource]
                          (user/fetch-by-google-id tx google-id))]
      (is (= user returned-user)))))
