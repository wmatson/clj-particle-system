(ns particle-system.common-math
  (:require [quil.core :as q]))

(defn angle->coords [angle magnitude]
  [(* magnitude (q/cos angle))
   (* magnitude (q/sin angle))])


(defn vector-add [v1 v2]
  (map + v1 v2))

(defn vector-decay [v rate]
  (map #(* % rate) v))
