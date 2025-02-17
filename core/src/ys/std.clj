;; Copyright 2023-2024 Ingy dot Net
;; This code is licensed under MIT license (See License for details)

;; This is the YAMLScript standard library.

(ns ys.std
  (:require
   [yamlscript.debug]
   [clojure.pprint :as pp]
   [clojure.string :as str])
  (:refer-clojure :exclude [print]))

(defn www [& xs]
  (apply yamlscript.debug/www xs))

(defn xxx [& xs]
  (apply yamlscript.debug/xxx xs))

(defn yyy [& xs]
  (apply yamlscript.debug/yyy xs))

(defn zzz [& xs]
  (apply yamlscript.debug/zzz xs))

(defn toBool [x] (boolean x))

(defn toFloat [x] (parse-double x))

(defn toInt [x] (parse-long x))

(defn toMap
  ([] {})
  ([x] (apply hash-map x))
  ([k v & xs] (apply hash-map k v xs)))

(defn toStr [& xs] (apply str xs))

; toList
; toNum
; toVec

(defn _get [coll key]
  (if (string? key)
    (or (get coll (keyword key))
      (get coll key))
    (get coll key)))

(defn __ [x & xs]
  (reduce _get x xs))

(defn _+ [x & xs]
  (cond
    (string? x) (apply str x xs)
    (vector? x) (apply concat x xs)
    (map? x) (apply merge x xs)
    :else (apply + x xs)))

(defn _* [x y]
  (if (and (string? x) (number? y))
    (apply str (repeat y x))
    (if (and (string? y) (number? x))
      (apply str (repeat x y))
      (* x y))))

(defn =-- [str rgx]
  (re-find rgx str))

(defn die [msg]
  (throw (Exception. ^String msg)))

(defmacro each [bindings & body]
  `(do
     (doall (for [~@bindings] (do ~@body)))
     nil))

(defn err [& xs]
  (binding [*out* *err*]
    (apply clojure.core/print xs)
    (flush)))

(defn join
  ([xs] (join "" xs))
  ([sep & xs]
    (if (= 1 (count xs))
      (str/join sep (first xs))
      (str/join sep xs))))

(defn out [& xs]
  (apply clojure.core/print xs)
  (flush))

(defn pow [x y]
  (Math/pow x y))

(defn pp [o]
  (pp/pprint o))

(defn pretty [o]
  (str/trim-newline
    (with-out-str
      (pp/pprint o))))

(defn print [o]
  (clojure.core/print o)
  (flush))

(defn rng [x y]
  (if (> y x)
    (range x (inc y))
    (range x (dec y) -1)))

(defn say [& xs]
  (apply clojure.core/println xs))

(defn warn [& xs]
  (binding [*out* *err*]
    (apply clojure.core/println xs)
    (flush)))

(comment)
