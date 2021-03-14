(ns cardlets.db.cards
  (:require [datomic.api :as d]
            [clojure.spec.alpha :as s]
            [cardlets.validation :as v]))

(s/def :card/id uuid?)
(s/def :card/front (v/text-longer-than 5))
(s/def :card/back (v/text-longer-than 5))
(s/def :card/progress (s/and #(> % 0) int?))
(s/def :card/next-study-date inst?)

(s/def ::card
  (s/keys :req [:card/front :card/back]
          :opt [:card/id :card/progress :card/next-study-date]))

(defn create! [conn user-id deck-id card-data])

(defn fetch-by-deck [db deck-id]
  (d/q '[:find [(pull ?card [*]) ...]
         :in $ ?did
         :where [?deck :deck/id ?did]
         [?card :card/deck ?deck]] db deck-id))



;; (defn fetch-list-by-user [db user-id]
;;   (d/q '[:find [(pull ?deck [*]) ...]
;;          :in $ ?uid
;;          :where
;;          [?user :user/id ?uid]
;;          [?deck :deck/author ?user]] db user-id))

