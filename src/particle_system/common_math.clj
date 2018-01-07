(ns particle-system.common-math
  (:require [quil.core :as q]))

(defn angle->coords [angle magnitude]
  [(* magnitude (q/cos angle))
   (* magnitude (q/sin angle))])
