(ns particle-system.scenes.spraying-circle
  (:require [particle-system.common-math :as cm]
            [quil.core :as q]
            [particle-system.particle-aging :as pa]))

(defn velocity-towards-center [coords min-factor max-factor]
  (map #(* (q/random min-factor max-factor)
           (- 0 %))
       coords))

(defn create-particle [state]
  (let [[x y] (cm/angle->coords (:angle state) 150)]
    {:coords [x y]
     :velocity (velocity-towards-center [x y] 0.01 0.03)
     :size [10 10]
     :life 100
     :alpha 255
     :color (:color state)
     :outline [255 255 255 0]}))

(defn age-fn [particle state]
  (-> particle
      (pa/fade-opacity 4)
      (pa/velocity-movement)
      (pa/shrink 0.98)
      (pa/simple-friction 0.99)))

(defn draw-fn [{:keys [color alpha coords size] :as  particle}]
  (q/stroke 0)
  (q/fill color 255 255 alpha)
  (apply q/ellipse (concat coords size)))

(def p-system-def {:max-particles 300
                   :burst 3
                   :emit-delay 1
                   :age-fn age-fn
                   :emit-fn create-particle
                   :draw-fn draw-fn})
