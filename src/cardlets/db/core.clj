(ns cardlets.db.core
  (:require [datomic.api :as d]
            [config.core :refer [env]]
            [cardlets.db.schema :refer [schema]]))

(defn create-conn [db-uri]
  (when db-uri
    (d/create-database db-uri)
    (let [conn (d/connect db-uri)]
      conn)))

(def conn
  (create-conn (:db-uri env)))

(comment
  (def tx @(d/transact conn schema)))



