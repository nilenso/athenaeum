(ns athenaeum.domain.user-test
  (:require [clojure.test :refer :all]
            [athenaeum.fixtures :as fixtures]
            [athenaeum.test-utils :as tu]
            [athenaeum.db :as db]
            [athenaeum.domain.user :as user]))

(use-fixtures :once fixtures/load-config fixtures/set-datasource)
(use-fixtures :each fixtures/clear-tables)

(deftest fetch-by-google-id-test
  (testing "If record with given google id exists, return it"
    (let [google-id "test-id"
          user (db/with-transaction [tx @db/datasource]
                 (user/create tx {:sub     google-id
                                  :name    "name"
                                  :email   "email"
                                  :picture "picture"}))
          returned-user (db/with-transaction [tx @db/datasource]
                          (user/fetch-by-google-id tx google-id))
          remove-id #(dissoc % :id)]
      (is (= (remove-id user) (remove-id returned-user)))))

  (testing "If record with given google id does not exist, return nil"
    (tu/clear-tables)
    (let [google-id-1 "test-id-1"
          _user (db/with-transaction [tx @db/datasource]
                  (user/create tx {:sub     google-id-1
                                   :name    "name"
                                   :email   "email"
                                   :picture "picture"}))
          google-id-2 "test-id-2"
          returned-user (db/with-transaction [tx @db/datasource]
                          (user/fetch-by-google-id tx google-id-2))]
      (is (= nil returned-user)))))
