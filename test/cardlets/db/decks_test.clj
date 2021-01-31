(ns cardlets.db.decks-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [datomic.api :as d]
            [cardlets.db.decks :as SUT]
            [cardlets.db.user :as user-ns]
            [cardlets.db.with-db :refer [with-db *conn*]]
            [clojure.test.check.generators :as gen]
            [clojure.spec.alpha :as s]))

(use-fixtures :each with-db)

(deftest decks

  (testing "list decks - returns a empty vector if no available cards"
    (let [user-data (gen/generate (s/gen ::user-ns/user))
          uid (user-ns/create! *conn* user-data)
          deck-data {:deck/id (d/squuid)
                     :deck/title "Learning Clojure"
                     :deck/tags #{"Programming" "Software" "Clojure"}
                     :deck/author [:user/id uid]}]
      @(d/transact *conn* [deck-data])
      (let [decks (SUT/fetch-list-by-user (d/db *conn*) uid)]
        (is (vector? decks))
        (is (= 1 (count decks))))))

  (testing "list decks - returns a vector of cards if available"
    (let [user-data (gen/generate (s/gen ::user-ns/user))
          uid (user-ns/create! *conn* user-data)
          decks (SUT/fetch-list-by-user (d/db *conn*) uid)]
      (is (vector? decks))
      (is (empty? decks)))))


