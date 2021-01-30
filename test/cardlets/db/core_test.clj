(ns cardlets.db.core-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [datomic.api :as d]
            [cardlets.db.with-db :refer [*conn* with-db]]
            [cardlets.db.core :as SUT]))

(deftest connection
  (testing "Create Connection"
    (is (not (nil? *conn*)))))

(use-fixtures :each with-db)
