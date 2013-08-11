(defproject ephemeral_front "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [hiccup "1.0.4"]
                 [compojure "1.1.5"]]
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler ephemeral_front.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
