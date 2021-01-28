(ns athenaeum.domain.user-test
  (:require [clojure.test :refer :all]
            [athenaeum.fixtures :as fixtures]
            [athenaeum.db :as db]
            [athenaeum.domain.user :as user]
            [athenaeum.test-utils :as tu]))

(use-fixtures :once fixtures/load-config fixtures/set-datasource)
(use-fixtures :each fixtures/clear-tables)

(deftest fetch-by-id-test
  (testing "If record with given id exists, return it"
    (let [user (db/with-transaction [tx @db/datasource]
                 (user/create tx {:google-id "google-id"
                                  :name      "name"
                                  :email     "email"}))
          user-id (:id user)
          returned-user (db/with-transaction [tx @db/datasource]
                          (user/find-by-id tx user-id))]
      (is (= user returned-user))))

  (tu/with-fixtures
    [fixtures/clear-tables]
    (testing "If record with given id does not exist, return nil"
      (let [returned-user (db/with-transaction [tx @db/datasource]
                            (user/find-by-id tx 1))]
        (is (= nil returned-user))))))

(deftest fetch-by-google-id-test
  (testing "If record with given google id does not exist, return nil"
    (let [returned-user (db/with-transaction [tx @db/datasource]
                          (user/find-by-google-id tx "test-id"))]
      (is (= nil returned-user))))

  (testing "If record with given google id exists, return it"
    (tu/with-fixtures
      [fixtures/clear-tables]
      (let [google-id "test-id"
            user (db/with-transaction [tx @db/datasource]
                   (user/create tx {:google-id google-id
                                    :name      "name"
                                    :email     "email"}))
            returned-user (db/with-transaction [tx @db/datasource]
                            (user/find-by-google-id tx google-id))]
        (is (= user returned-user))))))
