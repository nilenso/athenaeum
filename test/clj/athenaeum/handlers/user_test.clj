(ns athenaeum.handlers.user-test
  (:require [clojure.test :refer :all]
            [athenaeum.fixtures :as fixtures]
            [athenaeum.handlers.user :as user]
            [athenaeum.session :as session]
            [athenaeum.test-utils :as tu]))

(use-fixtures :once fixtures/load-config fixtures/set-datasource fixtures/set-redis-server-conn)
(use-fixtures :each fixtures/clear-tables fixtures/clear-sessions)

(deftest login-test
  (testing "On successful id token verification,
  creates session, adds id-token cookie to response and returns status 200"
    (let [req {:headers {:id-token "valid-id-token"}}
          res (with-redefs [user/verify-id-token (constantly {:google-id "google-id"
                                                              :name      "name"
                                                              :email     "name@nilenso.com"
                                                              :domain    "nilenso.com"
                                                              :photo-url "picture"})
                            session/new-id (constantly "new-session-id")]
                (user/login req))]
      (is (= true (session/exists? "new-session-id")))
      (is (= "new-session-id" (get-in res [:cookies "session-id" :value])))
      (is (= 200 (:status res)))))

  (testing "If email domain is not nilenso.com, returns status 401 "
    (tu/with-fixtures
      [fixtures/clear-tables fixtures/clear-sessions]
      (let [req {:headers {:id-token "valid-id-token"}}
            res (with-redefs [user/verify-id-token (constantly {:google-id "google-id"
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
      (is (= "login failed" (get-in res [:body :message])))))

  (testing "If id-token header is missing, returns status 400"
    (let [req {}
          res (user/login req)]
      (is (= 400 (:status res)))
      (is (= "id token header missing" (get-in res [:body :message]))))))

(deftest logout-test
  (testing "If session id in cookie has corresponding session, deletes it and returns status 200"
    (let [session-id (with-redefs [session/new-id (constantly "valid-session-id")]
                       (session/create "valid-session"))
          req {:cookies {"session-id" {:value session-id}}}
          res (user/logout req)]
      (is (= 200 (:status res)))
      (is (= false (session/exists? session-id)))))

  (testing "If session doesn't exist, returns status 400"
    (tu/with-fixtures
      [fixtures/clear-sessions]
      (let [req {:cookies {"session-id" {:value "invalid-session-id"}}}
            res (user/logout req)]
        (is (= 400 (:status res)))
        (is (= "session doesn't exist" (get-in res [:body :message]))))))

  (testing "If session id cookie doesn't exist, returns status 400"
    (let [req {}
          res (user/logout req)]
      (is (= 400 (:status res)))
      (is (= "session id cookie missing" (get-in res [:body :message]))))))
