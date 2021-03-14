(ns cardlets.validation
  (:require [clojure.spec.alpha :as s]))

(defn rand-str [len]
  (apply str (take len (repeatedly #(char (+ (rand 26) 65))))))

(defn email? [email]
  (and (string? email)
       (re-matches
        #"^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$"
        email)))

(defn string-longer-than [length]
  (fn [text] (and (string? text) #(> (count %) length))))

(def user-email
  (s/with-gen email?
    #(s/gen #{"joaopedro@email.com" "foo@gmail.com"})))

(defn text-longer-than [length]
  (s/with-gen (string-longer-than length)
    #(s/gen #{(rand-str (inc length)) (rand-str (inc (inc length)))})))
