(ns particle-system.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [particle-system.dynamic :as psd]))

(defn -main []
  (q/defsketch particle-system
    :title "You spin my circle right round"
    :size [800 500]
                                        ; setup function called only once, during sketch initialization.
    :setup psd/setup
                                        ; update-state is called on each iteration before draw-state.
    :update psd/update-state
    :key-pressed psd/on-keyboard-event
    :draw psd/draw-state
    :features [:keep-on-top]
                                        ; This sketch uses functional-mode middleware.
                                        ; Check quil wiki for more info about middlewares and particularly
                                        ; fun-mode.
    :middleware [m/fun-mode
                 m/pause-on-error]))

