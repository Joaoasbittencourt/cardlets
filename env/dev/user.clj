(ns user
  (:require [mount.core :as mount]
            [cardlets.db.core]))

(defn start []
  (mount/start))

(defn stop []
  (mount/stop))

(defn restart-dev []
  (stop)
  (start))

(comment (restart-dev))
