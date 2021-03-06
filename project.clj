(defproject athenaeum "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring "1.8.2"]
                 [bidi "2.1.6"]
                 [camel-snake-kebab "0.4.2"]
                 [seancorfield/next.jdbc "1.1.613"]
                 [org.postgresql/postgresql "42.2.18"]
                 [migratus "1.3.3"]
                 [ring/ring-json "0.5.0"]
                 [com.google.api-client/google-api-client "1.31.1"]
                 [com.taoensso/carmine "3.1.0"]
                 [aero "1.1.6"]]
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
                       :dependencies ^:replace [[org.clojure/clojure "1.10.1"]
                                                [thheller/shadow-cljs "2.11.8"]
                                                [re-frame "1.1.2"]
                                                [bidi "2.1.6"]
                                                [kibu/pushy "0.3.8"]
                                                [day8.re-frame/test "0.1.5"]
                                                [day8.re-frame/re-frame-10x "0.7.0"]
                                                [day8.re-frame/http-fx "0.2.2"]]}}
  :cljfmt {:paths ["src" "test"]})
