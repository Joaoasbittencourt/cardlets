(ns user
  (:require [mount.core :as mount]
            [cardlets.db.core]
            [clojure.tools.namespace.repl :as tn]))

(defn start []
  (mount/start))

(defn stop []
  (mount/stop))

(defn restart-dev []
  (stop)
  (tn/refresh-all)
  (start))

(comment (restart-dev))
