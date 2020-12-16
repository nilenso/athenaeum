(ns athenaeum-web.events-test
  (:require [cljs.test :refer :all]))

#_(deftest set-current-page-test
    (testing "When set-current-page event is dispatched, current page is set in db"
      (rf/dispatch [::e/set-current-page {:handler :test}])
      (is (= {:handler :test} @(rf/subscribe ::s/current-page)))))

(deftest a-failing-test
  (is (= 1 2)))

(a-failing-test)
