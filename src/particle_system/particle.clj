(ns particle-system.particle
  (:require [quil.core :as q]
            [particle-system.common-math :as cm]))

(def max-particles 300)

(defn update-particle [particle state age-fn]
  (-> particle
      (age-fn state)
      (update :life dec)))
      

(defn draw-particle [particle]
  (when (pos? (:life particle))
    (q/fill (:color particle) 255 255 (:alpha particle 255))
    (apply q/ellipse (concat (:coords particle) (:size particle [1 1])))))

(defn remove-dead [particles]
  (remove #(neg? (:life %)) particles))

(defn spawn-particles [emit-fn state particles]
  (if (< (count particles) max-particles)
    (conj particles (emit-fn state))
    particles))

(defn update-particles [particles state emit-fn age-fn]
  (->> particles
       remove-dead
       (spawn-particles emit-fn state)
       (map #(update-particle % state age-fn))))

(defn draw-particles [particles]
  (mapv draw-particle particles))
