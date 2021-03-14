(ns cardlets.db.cards-test
  (:require [clojure.test :refer [is deftest testing use-fixtures]]
            [cardlets.db.with-db :refer [with-db *conn*]]
            [clojure.spec.alpha :as s]
            [datomic.api :as d]
            [cardlets.utils.mock :as mock]
            [cardlets.db.cards :as SUT]
            [cardlets.db.users :as users]
            [cardlets.db.decks :as decks]))


(use-fixtures :each with-db)

(deftest cards

;;   (testing "Create Card"
;;     (let [uid (users/create! *conn* (mock/user))
;;           did (decks/create! *conn* uid (mock/deck))
;;           cid (SUT/create! *conn* uid did (mock/card))]))

  (testing "fetch cards by deck that returns empty when no cards available"
    (let [uid (users/create! *conn* (mock/user))
          did (decks/create! *conn* uid (mock/deck))
          cards (SUT/fetch-by-deck (d/db *conn*) did)]
      (is (empty? cards))
      (is (vector? cards))))

  (testing "fetch cards by deck that returns empty when cards available"
    (let [uid (users/create! *conn* (mock/user))
          did (decks/create! *conn* uid (mock/deck))
          card-data {:card/id (d/squuid)
                     :card/deck [:deck/id did]
                     :card/front "What is Clojure"
                     :card/back "A programming language"}]
      @(d/transact *conn* [card-data])
      (let [cards  (SUT/fetch-by-deck (d/db *conn*) did)]
        (is (seq cards))
        (is (s/valid? ::SUT/card (first cards)))
        (is (> (count cards) 0))))))
