(defproject athenaeum "0.1.0-SNAPSHOT"
  :description "Track your books"
  :url "https://github.com/nilenso/athenaeum"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring "1.8.2"]
                 [bidi "2.1.6"]]
  :plugins [[lein-cljfmt "0.7.0"]]
  :profiles {:uberjar {:aot :all}}
  :main clj.athenaeum.core)
