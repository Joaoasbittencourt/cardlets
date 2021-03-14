(ns cardlets.db.users-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [datomic.api :as d]
            [cardlets.db.users :as SUT]
            [cardlets.db.with-db :refer [with-db *conn*]]
            [clojure.spec.alpha :as s]
            [cardlets.utils.mock :as mock]))

(use-fixtures :each with-db)

(deftest user

  (testing "Creating User"
    (let [data (mock/user)
          res (SUT/create! *conn* data)]
      (is (not (nil? res)))
      (is (uuid? res))))

  (testing "User fetching"
    (let [uid (SUT/create! *conn* (mock/user))
          user (SUT/fetch (d/db *conn*) uid)]
      (is (= true (s/valid? ::SUT/user user)))))

  (testing "User Updating"
    (let [intended-data {:user/username "Edited"
                         :user/email "edited@mail.com"}
          uid (SUT/create! *conn* (mock/user))
          user (SUT/update! *conn* uid intended-data)]
      (is (s/valid? ::SUT/user user))
      (is (= (:user/username intended-data) (:user/username user)))
      (is (= (:user/email intended-data) (:user/email user)))))

  (testing "User delete"
    (let [uid (SUT/create! *conn* (mock/user))
          deleted-user (SUT/delete! *conn* uid)]
      (is (s/valid? ::SUT/user deleted-user))
      (is (nil? (SUT/fetch (d/db *conn*) uid))))))
