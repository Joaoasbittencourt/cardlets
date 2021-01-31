(ns cardlets.db.schema)

(def schema
  ;; User
  [{:db/ident :user/id
    :db/valueType :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/doc "User ID"}
   {:db/ident :user/email
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/doc "User Email"}
   {:db/ident :user/username
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "User username"}
   {:db/ident :user/password
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Hashed User Password"}
   {:db/ident :user/token
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "User Token"}
   {:db/ident :user/last-login
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc "User last login"}

  	;; DECK
   {:db/ident :deck/id
    :db/valueType :db.type/uuid
    :db/unique :db.unique/identity
    :db/cardinality :db.cardinality/one
    :db/doc "Deck ID"}
   {:db/ident :deck/author
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Deck author"}
   {:db/ident :deck/title
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Deck title"}
   {:db/ident :deck/tags
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/many
    :db/doc "Deck tags"}])
