(ns cardlets.db.user-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [cardlets.db.user :as SUT]
            [cardlets.db.with-db :refer [with-db *conn*]]
            [clojure.test.check.generators :as gen]
            [clojure.spec.alpha :as s]))


(use-fixtures :each with-db)

(deftest user
  (testing "Creating User"
    (let [data (gen/generate (s/gen ::SUT/user))
          res (SUT/create! *conn* data)]
      (is (not (nil? res)))
      (is (uuid? res)))))
