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

(s/def ::update-card
  (s/keys
   :opt [:card/progress :card/next-study-date :card/front :card/back]))


(defn fetch-by-deck [db deck-id]
  (d/q '[:find [(pull ?card [*]) ...]
         :in $ ?did
         :where
         [?deck :deck/id ?did]
         [?card :card/deck ?deck]] db deck-id))

(defn fetch [db card-id]
  (d/q '[:find (pull ?card [*]) .
         :in $ ?cid
         :where
         [?card :card/id ?cid]] db card-id))

(defn create! [conn deck-id card-data]
  (if (s/valid? ::card card-data)
    (let [card-id (d/squuid)
          tx-data (merge card-data {:card/id card-id
                                    :card/deck [:deck/id deck-id]})]
      (d/transact conn [tx-data])
      card-id)
    (throw (ex-info "Card is invalid"
                    {:cardlets/error-id :validation
                     :error "Invalid Card"}))))

(defn update! [conn card-id update-card-data]
  (if (s/valid? ::update-card update-card-data)
    (let [card (fetch (d/db conn) card-id)]
      (if (s/valid? ::card card)
        (let [tx-data (merge update-card-data
                             {:card/id card-id})
              db-after (:db-after @(d/transact conn [tx-data]))]
          (fetch db-after card-id))
        (throw (ex-info "No valid card was found"
                        {:cardlets/error-id :validation
                         :error "card not found"}))))
    (throw (ex-info "Update card data is invalid"
                    {:cardlets/error-id :validation
                     :error "Invalid card data"}))))

(defn delete! [conn card-id]
  (let [card (fetch (d/db conn) card-id)]
    (d/transact conn [[:db/retractEntity [:card/id card-id]]])
    card))


