(ns cardlets.db.with-db
  (:require
   [datomic.api :as d]
   [cardlets.db.schema :refer [schema]]
   [cardlets.db.core :as SUT]))

(def ^:dynamic *conn* nil)

(defn fresh-db []
  (let [db-uri (str "datomic:mem://" (gensym))
        conn (SUT/create-conn db-uri)]
    (d/transact conn schema)
    conn))

(defn with-db [f]
  (binding [*conn* (fresh-db)] (f)))
