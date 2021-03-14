(ns cardlets.db.cards-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [datomic.api :as d]
            [cardlets.db.cards :as SUT]
            [cardlets.db.with-db :refer [with-db *conn*]]
            [clojure.test.check.generators :as gen]
            [clojure.spec.alpha :as s]))


(use-fixtures :each with-db)

(deftest user
  (testing "Creating User"
    (is true)))
