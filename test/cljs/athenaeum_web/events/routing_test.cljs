(ns athenaeum-web.events.routing-test
  (:require [cljs.test :refer-macros [deftest testing is run-tests]]
            [day8.re-frame.test :as rf-test]
            [re-frame.core :as rf]
            [athenaeum-web.app.events.routing :as routing-events]
            [athenaeum-web.app.subscriptions :as subs]
            [athenaeum-web.test-utils :as tu]
            [athenaeum-web.home-page.events :as home-page-events]))

(deftest set-current-page-test
  (testing "When dispatched, only current page is set in db if associated on-route-change event is nil"
    (rf-test/run-test-sync
     (let [current-page {:handler :test}
           stubbed-event (tu/stub-event ::test-page-navigated)]
       (rf/dispatch [::routing-events/set-current-page current-page])
       (is (= current-page @(rf/subscribe [::subs/current-page])))
       (is (= nil (routing-events/on-route-change-event current-page)))
       (is (= nil @stubbed-event)))))

  (testing "When dispatched, current page is set in db and any associated non-nil on-route-change event is dispatched"
    (rf-test/run-test-sync
     (let [current-page {:handler :home-page}
           stubbed-event (tu/stub-event ::home-page-events/home-page-navigated)]
       (rf/dispatch [::routing-events/set-current-page current-page])
       (is (= current-page @(rf/subscribe [::subs/current-page])))
       (is (= ::home-page-events/home-page-navigated (routing-events/on-route-change-event current-page)))
       (is (= [::home-page-events/home-page-navigated] @stubbed-event))))))
