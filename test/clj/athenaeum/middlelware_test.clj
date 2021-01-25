(ns athenaeum.middlelware-test
  (:require [clojure.test :refer :all]
            [athenaeum.middleware :as middleware]
            [ring.util.response :as response]))

(deftest wrap-require-session-id-cookie*-test
  (testing "If session id cookie exists, calls wrapped handler"
    (let [rq {:cookies {:session-id {:value "session-id"}}}
          handler (fn [_] (response/response "success"))]
      (is (= {:status  200
              :headers {}
              :body    "success"}
             ((middleware/wrap-require-session-id-cookie* handler) rq)))))

  (testing "If session id cookie does not exist, returns status 400"
    (let [rq {}
          handler (fn [_] (response/response "success"))]
      (is (= {:status  400
              :headers {}
              :body    {:message "session id cookie missing"}}
             ((middleware/wrap-require-session-id-cookie* handler) rq))))))

(deftest wrap-require-id-token-header*-test
  (testing "If id token header exists, calls wrapped handler"
    (let [rq {:headers {:id-token "id-token"}}
          handler (fn [_] (response/response "success"))]
      (is (= {:status  200
              :headers {}
              :body    "success"}
             ((middleware/wrap-require-id-token-header* handler) rq)))))

  (testing "If id token header does not exist, returns status 400"
    (let [rq {}
          handler (fn [_] (response/response "success"))]
      (is (= {:status  400
              :headers {}
              :body    {:message "id token header missing"}}
             ((middleware/wrap-require-id-token-header* handler) rq))))))

(deftest wrap-keywordize-cookies-and-headers
  (testing "Takes handler and keywordizes the cookies and headers in the request passed to it"
    (let [rq1 {:headers {"foo" "bar"}}
          rq2 {:cookies {"foo" "bar"}}
          rq3 {:headers {"foo" "foo"} :cookies {"foo" {"bar" "bar"}}}
          handler (fn [rq] (response/response rq))]
      (is (= {:headers {:foo "bar"} :cookies nil}
             (->> rq1
                  ((middleware/wrap-keywordize-cookies-and-headers handler))
                  (:body))))
      (is (= {:cookies {:foo "bar"} :headers nil}
             (->> rq2
                  ((middleware/wrap-keywordize-cookies-and-headers handler))
                  (:body))))
      (is (= {:headers {:foo "foo"} :cookies {:foo {:bar "bar"}}}
             (->> rq3
                  ((middleware/wrap-keywordize-cookies-and-headers handler))
                  (:body)))))))

(deftest wrap-exception-handling-test
  (testing "Calls handler in a try block and responds with status 500 if it catches any exceptions"
    (let [rq {}
          handler (fn [_] (/ 1 0))]
      (is (= 500 (->> rq
                      ((middleware/wrap-exception-handling handler))
                      (:status))))))

  (testing "Simply calls handler if no exception is thrown"
    (let [rq {}
          handler (fn [_] (/ 1 1))]
      (is (= 1 (->> rq
                    ((middleware/wrap-exception-handling handler))))))))
