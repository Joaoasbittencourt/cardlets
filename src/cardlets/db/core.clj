(ns cardlets.db.core
  (:require [datomic.api :as d]
            [cardlets.config :refer [env]]
            [mount.core :as mount :refer [defstate]]
            [cardlets.db.schema :refer [schema]]))

(defn create-conn [db-uri]
  (when db-uri
    (d/create-database db-uri)
    (let [conn (d/connect db-uri)] conn)))

(declare conn)
(defstate conn
  :start (create-conn (:db-uri env))
  :stop (.release conn))

(comment
  (def tx @(d/transact conn schema)))

(comment
  (mount/start))
