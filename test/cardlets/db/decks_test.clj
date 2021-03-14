(ns cardlets.db.decks-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [datomic.api :as d]
            [cardlets.db.decks :as SUT]
            [cardlets.db.user :as user-ns]
            [cardlets.db.with-db :refer [with-db *conn*]]
            [clojure.test.check.generators :as gen]
            [clojure.spec.alpha :as s]))

(use-fixtures :each with-db)

(defn generate-user []
  (gen/generate (s/gen ::user-ns/user)))

(defn generate-deck
  ([]
   (gen/generate (s/gen ::SUT/deck)))
  ([author-uid]
   (merge (generate-deck)
          {:deck/author [:user/id author-uid]}))
  ([author-uid deck-id]
   (merge (generate-deck author-uid) {:deck/id deck-id})))

(deftest decks

  (testing "list decks - returns a empty vector when no cards avaliable"
    (let [user-data (generate-user)
          uid (user-ns/create! *conn* user-data)
          decks (SUT/fetch-list-by-user (d/db *conn*) uid)]
      (println decks)
      (is (vector? decks))
      (is (empty? decks))))

  (testing "list decks - returns a empty vector if no available cards"
    (let [user-data (generate-user)
          uid (user-ns/create! *conn* user-data)
          deck-data (generate-deck uid)]
      @(d/transact *conn* [deck-data])
      (let [decks (SUT/fetch-list-by-user (d/db *conn*) uid)]
        (is (vector? decks))
        (is (= 1 (count decks))))))

  (testing "fetching decks returns nil when not found"
    (let [user-params (generate-user)
          uid (user-ns/create! *conn* user-params)
          deck-id (d/squuid)
          deck-params (generate-deck uid deck-id)]
      @(d/transact *conn* [deck-params])
      (let [deck (SUT/fetch (d/db *conn*) uid deck-id)]
        (is (not (nil? deck))))))

  (testing "fetching decks returns a single deck when available"
    (let [user-params (generate-user)
          uid (user-ns/create! *conn* user-params)
          deck-id (d/squuid)
          deck (SUT/fetch (d/db *conn*) uid deck-id)]
      (is (nil? deck))))

  (testing "Creating a new deck"
    (let [user-params (generate-user)
          user-id (user-ns/create! *conn* user-params)
          deck-params (generate-deck)
          deck-id (SUT/create! *conn* user-id deck-params)]
      (is (uuid? deck-id)))))


