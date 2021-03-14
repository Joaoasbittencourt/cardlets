(ns cardlets.db.decks
  (:require [datomic.api :as d]
            [clojure.spec.alpha :as s]
            [cardlets.validation :as v]))

(s/def :deck/id uuid?)
(s/def :deck/title (v/text-longer-than 4))
(s/def :deck/tags (s/coll-of string?))

(s/def ::deck
  (s/keys :req [:deck/title :deck/tags]
          :opt [:deck/id]))

(s/def ::deck-edit
  (s/keys :opt [:deck/id :deck/title :deck/tags]))


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


(defn create! [conn uid deck-data]
  (if (s/valid? ::deck deck-data)
    (let [deck-id (d/squuid)
          tx-data (merge deck-data {:deck/id deck-id
                                    :deck/author [:user/id uid]})]
      (d/transact conn [tx-data])
      deck-id)
    (throw (ex-info "Deck is invalid"
                    {:cardlets/error-id :validation
                     :error "Invalid Deck"}))))

(defn edit! [conn user-id deck-id deck-data]
  (let [deck (fetch (d/db conn) user-id deck-id)]
    (if (and deck s/valid? ::deck-edit deck-data)
      (let [tx-data (merge deck-data {:deck/id deck-id})
            db-after (:db-after @(d/transact conn [tx-data]))]
        (fetch db-after user-id deck-id))
      (throw (ex-info
              "Unable to edit Deck"
              {:cardlets/error-id :server-error
               :error "Unable to edit Deck"})))))

(defn delete! [conn uid deck-id]
  (when-let [deck (fetch (d/db conn) uid deck-id)]
    (d/transact conn [[:db/retractEntity [:deck/id deck-id]]])
    deck))

