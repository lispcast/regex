(ns regex.core-test
  (:require [clojure.test :refer :all]
            [regex.core :refer :all]))

(defn regex= [a b]
  (= (str a) (str b)))

(deftest string-test
  (is (regex= #"abcd" (to-regex "abcd")))
  (is (regex= #"jjjj" (to-regex "jjjj"))))

(deftest character-test
  (is (regex= #"C" (to-regex \C))))

(deftest pattern-test
  (is (regex= #"[^a-z]+" (to-regex #"[^a-z]+"))))

(deftest vector-test
  (is (regex= #"abcd" (to-regex ["a" "b" "cd"])))
  (is (regex= #"abababab" (let [ab ["a" "b"]]
                            (to-regex [ab ab ab ab])))))

(deftest map-test
  (is (regex= #"[a-zA-Z]" (to-regex {\a \z
                                     \A \Z}))))

(deftest or-test
  (is (regex= #"a|b|c" (to-regex (OR "a" "b" "c")))))

(deftest complicated-test
  (is (regex= #"lispcast|hello|[0-9][a-z][a-z][a-z]"
        (let [digit {\0 \9}
              lowercase {\a \z}]
          (to-regex (OR "lispcast" #"hello"
                      [digit lowercase lowercase lowercase]))))))
