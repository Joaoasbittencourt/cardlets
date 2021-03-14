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
        (is (> (count cards) 0)))))


  (testing "fetch single cards returns nil when not available"
    (let [no-card-id (d/squuid)
          card (SUT/fetch (d/db *conn*) no-card-id)]
      (is (nil? card))
      (is (not (map? card)))))

  (testing "fetch single cards returns card when available"
    (let [uid (users/create! *conn* (mock/user))
          did (decks/create! *conn* uid (mock/deck))
          cid (d/squuid)
          card-data {:card/id cid
                     :card/deck [:deck/id did]
                     :card/front "What is Clojure"
                     :card/back "A programming language"}]
      @(d/transact *conn* [card-data])
      (let [card (SUT/fetch (d/db *conn*) cid)]
        (is (not (nil? card)))
        (is (s/valid? ::SUT/card card)))))

  (testing "Create Card returns a valid card"
    (let [uid (users/create! *conn* (mock/user))
          did (decks/create! *conn* uid (mock/deck))
          card-data {:card/id (d/squuid)
                     :card/deck [:deck/id did]
                     :card/front "What is Clojure"
                     :card/back "A programming language"}
          cid (SUT/create! *conn* did card-data)]
      (is (uuid? cid))
      (is (s/valid? ::SUT/card (SUT/fetch (d/db *conn*) cid)))))

  (testing "Card update to return a valid card"
    (let [uid (users/create! *conn* (mock/user))
          did (decks/create! *conn* uid (mock/deck))
          card-data {:card/id (d/squuid)
                     :card/deck [:deck/id did]
                     :card/front "What is Clojure"
                     :card/back "A programming language"}
          cid (SUT/create! *conn* did card-data)
          edit-data {:card/front "edited"}
          updated-card (SUT/update! *conn* cid edit-data)]
      (is (not (nil? updated-card)))
      (is (s/valid? ::SUT/card updated-card))
      (is (= "edited" (:card/front updated-card)))
      (is (not (= (:card/front card-data) (:card/front updated-card)))))))
