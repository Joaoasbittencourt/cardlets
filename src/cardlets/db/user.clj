(ns cardlets.db.user
  (:require [datomic.api :as d]
            [clojure.spec.alpha :as s]
            [cardlets.validation :as some]))

(s/def :user/id uuid?)
(s/def :user/email some/user-email)
(s/def :user/password (some/text-longer-than 5))
(s/def :user/username (some/text-longer-than 5))
(s/def :user/token  (some/text-longer-than 5))

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
       db  user-id
       '[*]))
