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
