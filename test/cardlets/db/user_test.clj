(ns cardlets.db.user-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [cardlets.db.user :as SUT]
            [cardlets.db.with-db :refer [with-db *conn*]]))


(use-fixtures :each with-db)

(deftest user
  (testing "Creating User"
    (let [user-data {:user/email "johndoe@email.com"
                     :user/password "123123"}
          res (SUT/create! *conn* user-data)]
      (is (not (nil? res)))
      (is (uuid? res)))))
