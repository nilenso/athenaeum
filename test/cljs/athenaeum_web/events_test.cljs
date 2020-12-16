(ns athenaeum-web.events-test
  (:require [cljs.test :refer-macros [deftest testing is run-tests]]
            [re-frame.core :as rf]
            [athenaeum-web.events :as e]
            [athenaeum-web.subscriptions :as s]))

(deftest initialize-db-test
  (testing "When db is initialized, current page should be home-page"
    (rf/dispatch-sync [::e/initialize-db])
    (is (= {:handler :home-page} @(rf/subscribe [::s/current-page])))))

(deftest set-current-page-test
  (testing "When set-current-page event is dispatched, current page is in db"
    (rf/dispatch-sync [::e/initialize-db])
    (rf/dispatch-sync [::e/set-current-page {:handler :test}])
    (is (= {:handler :test} @(rf/subscribe [::s/current-page])))))
