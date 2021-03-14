(ns cardlets.db.users
  (:require [datomic.api :as d]
            [clojure.spec.alpha :as s]
            [cardlets.validation :as v]))

(s/def :user/id uuid?)
(s/def :user/email v/user-email)
(s/def :user/password (v/text-longer-than 5))
(s/def :user/username (v/text-longer-than 5))
(s/def :user/token  (v/text-longer-than 5))

(s/def ::user
  (s/keys :req [:user/email :user/password]
          :opt [:user/id :user/token :user/username]))

(defn create! [conn params]
  (if (s/valid? ::user params)
    (let [user-id (d/squuid)
          tx-data (merge params {:user/id user-id})]
      (d/transact conn [tx-data])
      user-id)
    (throw (ex-info "User is invalid"
                    {:cardlets/error-id :validation
                     :error "Invalid email or password provided"}))))

(defn fetch [db user-id]
  (d/q '[:find (pull ?uid pattern) .
         :in $ ?user-id pattern
         :where [?uid :user/id ?user-id]]
       db  user-id '[*]))

(defn update! [conn user-id user-data]
  (if (fetch (d/db conn) user-id)
    (let [tx-data (merge user-data {:user/id user-id})
          db-after (:db-after @(d/transact conn [tx-data]))]
      (fetch db-after user-id))
    (throw (ex-info
            "Unable to update user"
            {:cardlets/error-id :server-error
             :error "Unable to edit user"}))))

(defn delete! [conn user-id]
  (when-let [user (fetch (d/db conn) user-id)]
    (d/transact conn [[:db/retractEntity [:user/id user-id]]])
    user))
