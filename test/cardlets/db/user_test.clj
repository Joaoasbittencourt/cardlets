(ns cardlets.db.user-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [datomic.api :as d]
            [cardlets.db.user :as SUT]
            [cardlets.db.with-db :refer [with-db *conn*]]
            [clojure.test.check.generators :as gen]
            [clojure.spec.alpha :as s]))


(use-fixtures :each with-db)

(defn fake-user []
  (gen/generate (s/gen ::SUT/user)))

(deftest user
  (testing "Creating User"
    (let [data (fake-user)
          res (SUT/create! *conn* data)]
      (is (not (nil? res)))
      (is (uuid? res))))
  (testing "User fetching"
    (let [uid (SUT/create! *conn* (fake-user))
          user (SUT/fetch (d/db *conn*) uid)]
      (println user)
      (is (= true (s/valid? ::SUT/user user))))))
