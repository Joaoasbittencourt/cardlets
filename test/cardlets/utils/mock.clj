(ns cardlets.utils.mock
  (:require
   [clojure.test.check.generators :as gen]
   [clojure.spec.alpha :as s]
   [cardlets.db.decks :as decks]
   [cardlets.db.users :as users]
   [cardlets.db.cards :as cards]))

(defn user []
  (gen/generate (s/gen ::users/user)))

(defn deck
  ([]
   (gen/generate (s/gen ::decks/deck)))
  ([author-uid]
   (merge (deck)
          {:deck/author [:user/id author-uid]}))
  ([author-uid deck-id]
   (merge (deck author-uid) {:deck/id deck-id})))

(defn card [deck-id]
  (merge (gen/generate (s/gen ::cards/card))
         {:card/deck [:deck/id deck-id]}))



