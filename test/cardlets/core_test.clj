(ns cardlets.core-test
  (:require [clojure.test :refer :all]))

(deftest random-sample
  (testing "1 + 1 = 2"
    (is (= 2 (+ 1 1)))))