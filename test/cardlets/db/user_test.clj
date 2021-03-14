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
      (is (= true (s/valid? ::SUT/user user)))))

  (testing "User Updating"
    (let [intended-data {:user/username "Edited"
                         :user/email "edited@mail.com"}
          uid (SUT/create! *conn* (fake-user))
          user (SUT/update! *conn* uid intended-data)]
      (is (s/valid? ::SUT/user user))
      (is (= (:user/username intended-data) (:user/username user)))
      (is (= (:user/email intended-data) (:user/email user)))))

  (testing "User delete"
    (let [uid (SUT/create! *conn* (fake-user))
          deleted-user (SUT/delete! *conn* uid)]
      (is (s/valid? ::SUT/user deleted-user))
      (is (nil? (SUT/fetch (d/db *conn*) uid))))))
