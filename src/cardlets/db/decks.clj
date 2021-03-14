(ns cardlets.db.decks
  (:require [datomic.api :as d]
            [clojure.spec.alpha :as s]
            [cardlets.validation :as v]))

(s/def :deck/id uuid?)
(s/def :deck/title (v/text-longer-than 4))
(s/def :deck/tags (s/coll-of string? :kind set? :min-count 0))

(s/def ::deck
  (s/keys :req [:deck/title :deck/tags]
          :opt [:deck/id]))


(defn fetch-list-by-user [db user-id]
  (d/q '[:find [(pull ?deck [*]) ...]
         :in $ ?uid
         :where
         [?user :user/id ?uid]
         [?deck :deck/author ?user]] db user-id))

(defn fetch
  "Fetch a Single deck by ID"
  [db user-id deck-id]
  (d/q '[:find (pull ?deck [*]) .
         :in $ ?uid ?did
         :where
         [?user :user/id ?uid]
         [?deck :deck/id ?did]
         [?deck :deck/author ?user]]
       db user-id deck-id))


(defn create! [db uid deck-data])
(defn edit [conn uid deck-data])
(defn delete! [conn uid deck-id])
