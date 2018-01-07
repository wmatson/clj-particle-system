(ns particle-system.particle
  (:require [quil.core :as q]
            [particle-system.common-math :as cm]))

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

(defn spawn-particles [{:keys [particles emit-fn max-particles] :as p-system}
                       state]
  (assoc p-system :particles
         (if (< (count particles) max-particles)
           (conj particles (emit-fn state))
           particles)))

(defn update-particles [{:keys [particles age-fn] :as p-system}
                        state]
  (assoc p-system :particles
         (map #(update-particle % state age-fn) particles)))

(defn update-p-system [{:keys [particles age-fn emit-fn] :as p-system}
                       state]
  (-> p-system
      (spawn-particles state)
      (update :particles remove-dead)
      (update-particles state)))

(defn draw-particles [particles]
  (mapv draw-particle particles))

(defn draw-p-system [{:keys [particles] :as p-system}]
  (draw-particles particles))
