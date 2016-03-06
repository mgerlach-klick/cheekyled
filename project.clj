(defproject cheekyled "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.hid4java/hid4java "0.4.0"]
                 [purejavahidapi/purejavahidapi "0.0.1"]]
  :main ^:skip-aot cheekyled.core
  :target-path "target/%s"
  :resource-paths ["./jars/jna-4.0.0.jar" "./jars/purejavahidapi-0.0.1.jar"]
  :profiles {:uberjar {:aot :all}})
