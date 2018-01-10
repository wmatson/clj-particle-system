(ns particle-system.dynamic
  (:require [quil.core :as q]
            [particle-system.particle :as p]
            [particle-system.common-math :as cm]
            [particle-system.particle-aging :as pa]
            [particle-system.scenes.spraying-circle]
            [particle-system.scenes.fire]))

;;Using the vars enables live-reload
(def p-systems
  [#'particle-system.scenes.spraying-circle/p-system-def
   #'particle-system.scenes.fire/p-system-def])

(defn live-reload-p-system [state curr-system]
  (merge curr-system @(first (:p-systems state))))

(defn rotate-left
  ([seq] (rotate seq 1))
  ([seq n]
   (let [size (count seq)
         rot (mod n size)]
     (->> (cycle seq)
          (drop rot)
          (take size)))))

(defn setup []
                                        ; Set frame rate to 30 frames per second.
  (q/frame-rate 60)
                                        ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
                                        ; setup function returns initial state. It contains
                                        ; circle color and position.
  {:color 0
   :angle 0
   :p-systems p-systems
   :p-system (p/init-system @(first p-systems))})

(defn on-keyboard-event [state event]
  (case (:key event)
    :left (update state :p-systems rotate-left 1)
    :right (update state :p-systems rotate-left -1)
    state))

(defn update-state [state]
  (assoc state
         :color (mod (inc (:color state)) 255)
         :angle (+ (:angle state) 0.05)
         :p-system (live-reload-p-system state (p/update-p-system (:p-system state) state))))

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
      ;; (q/ellipse x y 10 10)
      (p/draw-p-system (:p-system state)))))
