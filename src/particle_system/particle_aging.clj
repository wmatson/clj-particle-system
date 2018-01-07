(ns particle-system.particle-aging
  (:require [particle-system.common-math :as cm]))

(defn velocity-movement [particle]
  (update particle :coords
          cm/vector-add (:velocity particle)))

(defn fade-opacity
  ([particle]
   (fade-opacity particle 1))
  ([particle fade-amount]
   (update particle :alpha - fade-amount)))

(defn simple-friction [particle friction-factor]
  (update particle :velocity cm/vector-decay friction-factor))

(defn shrink
  ([particle]
   (shrink particle 0.99))
  ([particle shrink-factor]
   (update particle :size cm/vector-decay shrink-factor)))
