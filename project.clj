(defproject meals-api "0.1.0-SNAPSHOT"
  :description "core api for managing meals"
  :url "https://github.com/jcosentino11/meals-api"
  :license {:name "The MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :main mealsapi.core
  :aot :all
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :repl-options {:init-ns mealsapi.core})
