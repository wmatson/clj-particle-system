(ns particle-system.particle
  (:require [quil.core :as q]
            [particle-system.common-math :as cm]))

(defn draw-particle [particle]
  (when (pos? (:life particle))
    (apply q/stroke (:outline particle [0 0 0 0]))
    (q/fill (:color particle) 255 255 (:alpha particle 255))
    (apply q/ellipse (concat (:coords particle) (:size particle [1 1])))))

(def system-defaults
  {:max-particles 300
   :emit-delay 1
   :draw-fn draw-particle})

(defn init-system [system-def]
  (let [defaulted (merge system-defaults system-def)]
    (assoc defaulted
           :particles []
           :emit-timer (:emit-delay defaulted))))

(defn update-particle [particle state age-fn]
  (-> particle
      (age-fn state)
      (update :life dec)))

(defn remove-dead [particles]
  (remove #(neg? (:life %)) particles))

(defn spawn-particles [{:keys [particles emit-fn max-particles emit-delay emit-timer] :as p-system}
                       state]
  (if (neg? emit-timer)
    (let [to-add (min (:burst p-system 1)
                      (- max-particles (count particles)))]
      (assoc p-system
             :particles
             (concat particles (repeatedly to-add #(emit-fn state)))
             :emit-timer emit-delay))
    p-system))

(defn update-particles [{:keys [particles age-fn] :as p-system}
                        state]
  (assoc p-system :particles
         (map #(update-particle % state age-fn) particles)))

(defn update-p-system [{:keys [particles age-fn emit-fn] :as p-system}
                       state]
  (-> p-system
      (update :emit-timer dec)
      (spawn-particles state)
      (update :particles remove-dead)
      (update-particles state)))

(defn draw-p-system [{:keys [particles draw-fn] :as p-system}]
  (mapv draw-fn particles))
