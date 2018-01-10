(ns particle-system.scenes.fire
  (:require [particle-system.common-math :as cm]
            [quil.core :as q]
            [particle-system.particle-aging :as pa]))

(defn create-particle [state]
  (let [[x y] (cm/angle->coords (:angle state) 150)]
    {:coords [0 0];[x y]
     :velocity [(q/random -0.5 0.5) (q/random -3 0)] ;(velocity-towards-center [x y] 0.01 0.03)
     :size [10 10]
     :life 100
     :alpha 255
     :color 0;(:color state)
     :outline [255 255 255 0]}))

(defn age-fn [particle state]
  (-> particle
      (pa/fade-opacity 4)
      (pa/velocity-movement)
      (pa/shrink 0.99)
      (pa/simple-friction 0.99)))

(defn draw-fn [{:keys [color alpha coords] :as  particle}]
  (q/no-stroke)
  (q/fill color 255 255 alpha)
  (q/with-translation coords
    (q/with-rotation [0.25]
      (q/quad -5 0 0 -5 5 0 0 5))))

(def p-system-def {:max-particles 300
                   :burst 3
                   :emit-delay 1
                   :age-fn age-fn
                   :emit-fn create-particle
                   :draw-fn draw-fn})
