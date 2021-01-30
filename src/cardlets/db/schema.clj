(ns cardlets.db.schema)

(def schema
  [{:db/ident :user/id
    :db/valueType :db.type/uui
    :db/cardinality :db.type/uui
    :db/unique :db.unique/identity
    :db/doc "User ID"}
   {:db/ident :user/email
    :db/valueType :db.type/string
    :db/cardinality :db.type/one
    :db/unique :db.unique/identity
    :db/doc "User Email"}
   {:db/ident :user/username
    :db/valueType :db.type/string
    :db/cardinality :db.type/one
    :db/doc "User username"}
   {:db/ident :user/password
    :db/valueType :db.type/string
    :db/cardinality :db.type/one
    :db/doc "Hashed User Password"}
   {:db/ident :user/token
    :db/valueType :db.type/string
    :db/cardinality :db.type/one
    :db/doc "User Token"}
   {:db/ident :user/last-login
    :db/valueType :db.type/instant
    :db/cardinality :db.type/one
    :db/doc "Hasehd User Password"}
   ])