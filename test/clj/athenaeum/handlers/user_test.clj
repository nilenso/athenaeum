(ns athenaeum.handlers.user-test
  (:require [clojure.test :refer :all]
            [athenaeum.fixtures :as fixtures]
            [athenaeum.handlers.user :as user]
            [athenaeum.domain.user :as domain-user]
            [athenaeum.session :as session]
            [athenaeum.test-utils :as tu]
            [athenaeum.db :as db]))

(use-fixtures :once fixtures/load-config fixtures/set-datasource fixtures/set-redis-server-conn)
(use-fixtures :each fixtures/clear-tables fixtures/clear-sessions)

(deftest login-test
  (testing "On successful id token verification, creates session,
            adds id-token cookie to response and returns status 200"
    (let [req {:headers {:id-token "valid-id-token"}}
          res (with-redefs [user/verify-id-token (constantly true)
                            user/get-payload (constantly {:google-id "google-id"
                                                          :name      "name"
                                                          :email     "name@nilenso.com"
                                                          :domain    "nilenso.com"
                                                          :photo-url "picture"})
                            session/new-id (constantly "new-session-id")]
                (user/login req))]
      (is (= true (session/exists? "new-session-id")))
      (is (= "new-session-id" (get-in res [:cookies :session-id :value])))
      (is (= 200 (:status res)))))

  (testing "If email domain is not nilenso.com, returns status 401 "
    (tu/with-fixtures
      [fixtures/clear-tables fixtures/clear-sessions]
      (let [req {:headers {:id-token "valid-id-token"}}
            res (with-redefs [user/verify-id-token (constantly true)
                              user/get-payload (constantly {:google-id "google-id"
                                                            :name      "name"
                                                            :email     "name@foo.bar"
                                                            :domain    "foo.bar"
                                                            :picture   "picture"})
                              session/new-id (constantly "new-session-id")]
                  (user/login req))]
        (is (= 401 (:status res)))
        (is (= "invalid domain" (get-in res [:body :message]))))))

  (testing "If id token is invalid, returns status 400"
    (let [req {:headers {:id-token "invalid-id-token"}}
          res (user/login req)]
      (is (= 400 (:status res)))
      (is (= "id token verification failed" (get-in res [:body :message]))))))

(deftest logout-test
  (testing "Deletes session corresponding to session id in cookie and returns status 200"
    (let [session-id (with-redefs [session/new-id (constantly "valid-session-id")]
                       (session/create "valid-session"))
          req {:cookies {:session-id {:value session-id}}}
          res (user/logout req)]
      (is (= 200 (:status res)))
      (is (= false (session/exists? session-id))))))

(deftest user-test
  (testing "Looks up user from user id in the session corresponding to session id in cookie and returns the user"
    (let [user (db/with-transaction [tx @db/datasource]
                 (domain-user/create tx {:google-id "google id"
                                         :name      "name"
                                         :email     "email"}))
          user-id (:id user)
          session-id (with-redefs [session/new-id (constantly "valid-session-id")]
                       (session/create user-id))
          req {:cookies {:session-id {:value session-id}}}
          res (user/user req)]
      (is (= user (-> res
                      :body
                      :user))))))
