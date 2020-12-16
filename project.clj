(defproject athenaeum "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring "1.8.2"]
                 [bidi "2.1.6"]
                 [aero "1.1.6"]]
  :plugins [[lein-cljfmt "0.7.0"]]
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :main athenaeum.core
  :uberjar-exclusions [#"public/"]
  :uberjar-name "athenaeum-uber.jar"
  :jar-name "athenaeum.jar"
  :profiles {:uberjar {:aot :all}
             :cljs    {:source-paths ["src/cljs" "test/cljs"]
                       :dependencies [[org.clojure/clojure "1.10.1"]
                                      [thheller/shadow-cljs "2.11.8"]
                                      [reagent "1.0.0-rc1"]
                                      [kibu/pushy "0.3.8"]
                                      [re-frame "1.1.2"]]}}
  :cljfmt {:paths ["src" "test"]})
