(ns cardlets.config
  (:require [mount.core :refer [defstate]]
            [config.core :as config]))

(declare env)
(defstate env
  :start config/env)
