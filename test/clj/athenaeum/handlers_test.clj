(ns athenaeum.handlers-test
  (:require [clojure.test :refer :all]
            [athenaeum.handlers :as h]))

(deftest home-test
  (testing "Given any request, respond with status 200"
    (let [res (h/home "foo")]
      (is (= 200 (:status res)))
      (is (= "hi world" (:body res))))))

(deftest ping-test
  (testing "Given any request, respond with status 200"
    (let [res (h/ping "foo")]
      (is (= 200 (:status res)))
      (is (= "pong" (:body res))))))
