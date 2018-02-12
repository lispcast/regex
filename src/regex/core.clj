(ns regex.core
  (:require [clojure.string :as str]))

(defprotocol ToRegex
  (to-regex [x]))

(deftype Or [args]
  ToRegex
  (to-regex [_]
    (->> args
      (map to-regex)
      (str/join "|")
      to-regex)))

(defn OR [& args]
  (Or. args))

(extend-protocol ToRegex
  String
  (to-regex [s] (re-pattern s))
  Character
  (to-regex [c] (to-regex (str c)))
  java.util.regex.Pattern
  (to-regex [p] p)
  clojure.lang.PersistentVector
  (to-regex [v]
    (->> v
      (map to-regex)
      (apply str)
      to-regex))
  clojure.lang.APersistentMap
  (to-regex [m]
    (to-regex
      (str
        "["
        (apply str
          (for [[from to] m]
            (str from "-" to)))
        "]"))))

;; Answers to Exercises


;; 1. Define a new type called OneOrMore and a function plus that
;; creates an instance of OneOrMore. Extend the protocol so that
;; (to-regex (plus “xyz”)) creates the regex #”(xyz)+”.

(deftype OneOrMore [child]
  ToRegex
  (to-regex [_]
    (to-regex (str "("
                   (to-regex child)
                   ")+"))))

(defn plus [child]
  (OneOrMore. child))

;; 2. Define a new type called ZeroOrMore and a function star that
;; creates an instance of ZeroOrMore. Extend the protocol so that
;; (to-regex (star “xyz”)) creates the regex #”(xyz)*”

(deftype ZeroOrMore [child]
  ToRegex
  (to-regex [_]
    (to-regex (str "("
                   (to-regex child)
                   ")*"))))

(defn star [child]
  (ZeroOrMore. child))


;; 3. Define a new type called Optional and a function optional that
;; creates an instance of Optional. Extend the protocol so that
;; (to-regex (optional “xyz”)) creates the regex #”(xyz)?”.

(deftype Optional [child]
  ToRegex
  (to-regex [_]
    (to-regex (str "("
                   (to-regex child)
                   ")?"))))

(defn optional [child]
  (Optional. child))

;; 4. Grouping (using parentheses) in regular expressions is important
;; for getting parts out of a string. Define a way to group a
;; subexpression so that it is matched properly. For instance (re-find
;; (to-regex [(group “lisp”) “cast”]) “lispcast”) should return
;; [“lispcast” “lisp”].

(deftype Group [child]
  ToRegex
  (to-regex [_]
    (to-regex (str "("
                   (to-regex child)
                   ")"))))

(defn group [child]
  (Group. child))

;; 5. After defining grouping in this way, modify the regexes from
;; exercises 1-3 so that they use non-capturing groups (#”(?:…)”,
;; explained here
;; https://stackoverflow.com/questions/3512471/what-is-a-non-capturing-group-what-does-do).

(deftype OneOrMore [child]
  ToRegex
  (to-regex [_]
    (to-regex (str "(?:"
                   (to-regex child)
                   ")+"))))
(deftype ZeroOrMore [child]
  ToRegex
  (to-regex [_]
    (to-regex (str "(?:"
                   (to-regex child)
                   ")*"))))

(deftype Optional [child]
  ToRegex
  (to-regex [_]
    (to-regex (str "(?:"
                   (to-regex child)
                   ")?"))))
