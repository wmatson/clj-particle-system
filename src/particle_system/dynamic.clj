(ns particle-system.dynamic
  (:require [quil.core :as q]
            [particle-system.particle :as p]
            [particle-system.common-math :as cm]
            [particle-system.particle-aging :as pa]))

(defn velocity-towards-center [coords min-factor max-factor]
  (map #(* (q/random min-factor max-factor)
           (- 0 %))
       coords))

(defn create-particle [state]
  (let [[x y] (cm/angle->coords (:angle state) 150)]
    {:coords [x y]
     :velocity (velocity-towards-center [x y] 0.01 0.03)
     :size [20 20]
     :life 100
     :alpha 255
     :color (:color state)}))

(defn age-fn [particle state]
  (-> particle
      (pa/fade-opacity 4)
      (pa/velocity-movement)
      (pa/shrink 0.99)
      (pa/simple-friction 0.99)))
      
(def p-system-def {:max-particles 300
                   :burst 3
                   :emit-delay 1
                   :age-fn age-fn
                   :emit-fn create-particle})

(defn setup []
                                        ; Set frame rate to 30 frames per second.
  (q/frame-rate 60)
                                        ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
                                        ; setup function returns initial state. It contains
                                        ; circle color and position.
  {:color 0
   :angle 0
   :p-system (p/init-system p-system-def)})


(defn update-fns [curr-system]
  (merge curr-system p-system-def))

(defn update-state [state]
  {:color (mod (+ (:color state) 1) 255)
   :angle (+ (:angle state) 0.05)
   :p-system (update-fns (p/update-p-system (:p-system state) state))})

(defn draw-state [state]
                                        ; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  ; Set circle color.
  (q/fill (:color state) 255 255)

  ; Calculate x and y coordinates of the circle.
  (let [angle (:angle state)
        [x y] (cm/angle->coords angle 150)] 
    ; Move origin point to the center of the sketch.
    (q/with-translation [(/ (q/width) 2)
                         (/ (q/height) 2)]
      ; Draw the circle.
      (q/ellipse x y 10 10)
      (p/draw-p-system (:p-system state)))))
