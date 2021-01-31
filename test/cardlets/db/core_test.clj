(ns cardlets.db.core-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [cardlets.db.with-db :refer [*conn* with-db]]))

(deftest connection
  (testing "Create Connection"
    (is (not (nil? *conn*)))))

(use-fixtures :each with-db)
