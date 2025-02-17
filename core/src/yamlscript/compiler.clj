;; Copyright 2023-2024 Ingy dot Net
;; This code is licensed under MIT license (See License for details)

;; YAMLScript is a programming language that is hosted by Clojure platforms.
;; It can be used to add scripting to abilities to YAML files.

;; The yamlscript.compiler is responsible for converting YAMLScript code to
;; Clojure code. It does this by sending the input through a stack of 7
;; transformation libraries.

(ns yamlscript.compiler
  (:require
   [a0.patch-pprint]
   [clojure.pprint]
   [clojure.edn]
   [yamlscript.parser]
   [yamlscript.composer]
   [yamlscript.resolver]
   [yamlscript.builder]
   [yamlscript.transformer]
   [yamlscript.constructor]
   [yamlscript.printer]
   [yamlscript.debug :refer [www]])
  (:refer-clojure :exclude [compile]))

(def ^:dynamic *debug* {})

(def stages
  {"parse" true
   "compose" true
   "resolve" true
   "build" true
   "transform" true
   "construct" true
   "print" true})

(defn compile
  "Convert YAMLScript code string to an equivalent Clojure code string."
  [^String yamlscript-string]
  (->> yamlscript-string
    yamlscript.parser/parse
    yamlscript.composer/compose
    yamlscript.resolver/resolve
    yamlscript.builder/build
    yamlscript.transformer/transform
    yamlscript.constructor/construct
    yamlscript.printer/print))

(defn debug-print [stage data]
  (when (get *debug* stage)
    (println (str "*** " stage " output ***"))
    (clojure.pprint/pprint data)
    (println ""))
  data)

(defn compile-debug
  "Convert YAMLScript code string to an equivalent Clojure code string."
  [^String yamlscript-string]
  (->> yamlscript-string
    yamlscript.parser/parse
    (debug-print "parse")
    yamlscript.composer/compose
    (debug-print "compose")
    yamlscript.resolver/resolve
    (debug-print "resolve")
    yamlscript.builder/build
    (debug-print "build")
    yamlscript.transformer/transform
    (debug-print "transform")
    yamlscript.constructor/construct
    (debug-print "construct")
    yamlscript.printer/print
    (debug-print "print")))

(defn pretty-format [code]
  (->> code
    (#(str "(do " %1 "\n)\n"))
    read-string
    rest
    (map #(str
            (with-out-str (clojure.pprint/write %1))
            "\n"))
    (apply str)))

(comment
  www
  (->> "../test/hello.ys"
    slurp
    compile
    println)
  )
