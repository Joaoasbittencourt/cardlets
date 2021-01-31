(ns cardlets.db.decks
  (:require [datomic.api :as d]))

(defn fetch-list-by-user [db user-id]
  (d/q '[:find [(pull ?deck [*]) ...]
         :in $ ?uid
         :where
         [?user :user/id ?uid]
         [?deck :deck/author ?user]] db user-id))

(defn fetch [db uid deck-id])
(defn create! [db uid deck-data])
(defn edit [conn uid deck-data])
(defn delete! [conn uid deck-id])
