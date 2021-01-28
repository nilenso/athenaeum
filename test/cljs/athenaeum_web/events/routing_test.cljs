(ns athenaeum-web.events.routing-test
  (:require [cljs.test :refer-macros [deftest testing is run-tests]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [athenaeum-web.app.events.routing :as routing-events]
            [athenaeum-web.app.subscriptions :as subs]
            [athenaeum-web.test-utils :as tu]))

(deftest set-current-page-test
  (testing "When dispatched and auth-check returns an event, current page is set in db and event is dispatched"
    (rf-test/run-test-sync
     (let [current-page {:handler :test}
           stubbed-event (tu/stub-event ::test-page-navigated)]
       (with-redefs [routing-events/authentication-check (constantly [::test-page-navigated])]
         (rf/dispatch [::routing-events/set-current-page current-page]))
       (is (= current-page @(rf/subscribe [::subs/current-page])))
       (is (= [::test-page-navigated] @stubbed-event)))))

  (testing "When dispatched and auth-check returns an empty list, current page is simply set in db"
    (rf-test/run-test-sync
     (let [current-page {:handler :test}]
       (with-redefs [routing-events/authentication-check (constantly [])]
         (rf/dispatch [::routing-events/set-current-page current-page]))
       (is (= current-page @(rf/subscribe [::subs/current-page])))))))
