(ns cardlets.db.decks-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [datomic.api :as d]
            [cardlets.db.decks :as SUT]
            [cardlets.db.users :as users]
            [cardlets.db.with-db :refer [with-db *conn*]]
            [cardlets.utils.mock :as mock]
            [clojure.spec.alpha :as s]))

(use-fixtures :each with-db)

(deftest decks

  (testing "list decks - returns a empty vector when no cards avaliable"
    (let [user-data (mock/user)
          uid (users/create! *conn* user-data)
          decks (SUT/fetch-list-by-user (d/db *conn*) uid)]
      (is (vector? decks))
      (is (empty? decks))))

  (testing "list decks - returns a empty vector if no available cards"
    (let [user-data (mock/user)
          uid (users/create! *conn* user-data)
          deck-data (mock/deck uid)]
      @(d/transact *conn* [deck-data])
      (let [decks (SUT/fetch-list-by-user (d/db *conn*) uid)]
        (is (vector? decks))
        (is (= 1 (count decks))))))

  (testing "fetching decks returns nil when not found"
    (let [user-params (mock/user)
          uid (users/create! *conn* user-params)
          deck-id (d/squuid)
          deck-params (mock/deck uid deck-id)]
      @(d/transact *conn* [deck-params])
      (let [deck (SUT/fetch (d/db *conn*) uid deck-id)]
        (is (not (nil? deck))))))

  (testing "fetching decks returns a single deck when available"
    (let [user-params (mock/user)
          uid (users/create! *conn* user-params)
          deck-id (d/squuid)
          deck (SUT/fetch (d/db *conn*) uid deck-id)]
      (is (nil? deck))))

  (testing "Creating a new deck"
    (let [user-params (mock/user)
          user-id (users/create! *conn* user-params)
          deck-params (mock/deck)
          deck-id (SUT/create! *conn* user-id deck-params)]
      (is (uuid? deck-id))))

  (testing "Editing a new deck"
    (let [user-id (users/create! *conn* (mock/user))
          deck-params (mock/deck)
          deck-id (SUT/create! *conn* user-id deck-params)
          edit-deck-params {:deck/title "edited"}
          edited-deck (SUT/edit! *conn* user-id deck-id edit-deck-params)]
      (is (not (nil? edited-deck)))
      (is (= (:deck/title edited-deck) "edited"))))

  (testing "Deleting a deck"
    (let [user-id (users/create! *conn* (mock/user))
          deck-params (mock/deck)
          deck-id (SUT/create! *conn* user-id deck-params)
          deleted-deck (SUT/delete! *conn* user-id deck-id)
          user-try (SUT/fetch (d/db *conn*) user-id deck-id)]
      (is (s/valid? ::SUT/deck deleted-deck))
      (is (nil? user-try)))))
