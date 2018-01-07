(ns particle-system.dynamic
  (:require [quil.core :as q]
            [particle-system.particle :as p]
            [particle-system.common-math :as cm]))

(defn create-particle [state]
  (let [[x y] (cm/angle->coords (:angle state) 150)]
    {:coords [x y]
     :velocity [(* (q/random 0.01 0.03) (- 0 x))
                (* (q/random 0.01 0.03) (- 0 y))]
     :size [10 10]
     :life 200
     :alpha 255
     :color (:color state)}))

(defn age-fn [particle state]
  (-> particle
      (update :alpha dec)
      (update :velocity cm/vector-decay 0.99)
      (update :coords cm/vector-add (:velocity particle))))

(def p-system-def {:max-particles 300
                   :burst 1
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
