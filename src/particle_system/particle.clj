(ns particle-system.particle
  (:require [quil.core :as q]
            [particle-system.common-math :as cm]))

(def max-particles 300)

(defn update-particle [particle]
  (-> particle
      (update :life dec)
      (update :x-velocity * 0.95)
      (update :y-velocity * 0.95)
      (update :x + (:x-velocity particle 0))
      (update :y + (:y-velocity particle 0))))

(defn draw-particle [particle]
  (when (pos? (:life particle))
    (q/fill (:color particle) 255 255)
    (q/ellipse (:x particle) (:y particle) (:width particle 1) (:height particle 1))))

(defn remove-dead [particles]
  (remove #(neg? (:life %)) particles))

(defn spawn-particles [create-particle particles]
  (if (< (count particles) max-particles)
    (conj particles (create-particle))
    particles))

(defn update-particles [particles create-particle]
  (->> particles
       remove-dead
       (spawn-particles create-particle)
       (map update-particle)))

(defn draw-particles [particles]
  (mapv draw-particle particles))
