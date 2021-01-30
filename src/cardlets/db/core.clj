(ns cardlets.db.core
  (:require [datomic.api :as d]
            [config.core :refer [env]]
            [cardlets.db.schema :refer [schema]]))


(def database-uri "datomic:dev://localhost:4334/cardlets")
;; (def database-uri "datomic:sql://cardlets-development?jdbc:postgresql://localhost:4334/datomic?user=datomic&password=datomic")

(defn create-conn [db-uri]
  (d/create-database db-uri)
  (let [conn (d/connect db-uri)]
    conn))

(def conn (create-conn database-uri))

(comment
  (def tx @(d/transact conn schema)))

(println (keys env))
