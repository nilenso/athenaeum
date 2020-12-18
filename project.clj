(defproject athenaeum "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring "1.8.2"]
                 [bidi "2.1.6"]
                 [seancorfield/next.jdbc "1.1.613"]
                 [org.postgresql/postgresql "42.2.18"]
                 [migratus "1.3.3"]
                 [ring/ring-json "0.5.0"]]
  :plugins [[lein-cljfmt "0.7.0"]]
  :repl-options {:init-ns dev.repl-utils}
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :main athenaeum.core
  :uberjar-exclusions [#"public/" #"src/cljs/" #"test/"]
  :uberjar-name "athenaeum-uber.jar"
  :jar-name "athenaeum.jar"
  :profiles {:uberjar {:aot :all}
             :cljs    {:source-paths ["src/cljs" "test/cljs"]
                       :dependencies [[org.clojure/clojure "1.10.1"]
                                      [thheller/shadow-cljs "2.11.8"]
                                      [re-frame "1.1.2"]
                                      [kibu/pushy "0.3.8"]
                                      [day8.re-frame/test "0.1.5"]]}}
  :cljfmt {:paths ["src" "test"]})
