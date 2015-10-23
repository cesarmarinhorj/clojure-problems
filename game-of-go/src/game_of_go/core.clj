(ns game-of-go.core
  (:require [clojure.repl :as repl]
            [clojure.data.json :as json]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))

(def five-by-five [[:. :. :. :. :.]
                   [:. :. :. :. :.]
                   [:. :. :. :. :.]
                   [:. :. :. :. :.]
                   [:. :. :. :. :.]])

(defn move [board row-pos col-pos turn]
  {:board (if (empty-at? board row-pos col-pos)
            (place-in-row-col board row-pos col-pos turn)
            board)
   :turn (next-turn turn)})

(defn next-turn [current-turn]
  (if (= current-turn :w) :b :w))

(defn place-in-row-col [board row-pos col-pos turn]
  (let [row (nth board row-pos)]
    (assoc board
           row-pos
           (assoc row col-pos turn))))

(defn empty-at? [board row-pos col-pos]
  (= (nth (nth board row-pos) col-pos) :.))

(deftest placement
  (testing "simple placement"
    (is (= (:board (move [[:. :. :. :. :.]
                          [:. :. :. :. :.]
                          [:. :. :. :. :.]
                          [:. :. :. :. :.]
                          [:. :. :. :. :.]] 0 0 :w))

           [[:w :. :. :. :.]
            [:. :. :. :. :.]
            [:. :. :. :. :.]
            [:. :. :. :. :.]
            [:. :. :. :. :.]])))

  (testing "can't place at a location that already has a piece"
    (is (= (:board (move
                    [[:w :. :. :. :.]
                     [:. :. :. :. :.]
                     [:. :. :. :. :.]
                     [:. :. :. :. :.]
                     [:. :. :. :. :.]] 0 0 :b))

           [[:w :. :. :. :.]
            [:. :. :. :. :.]
            [:. :. :. :. :.]
            [:. :. :. :. :.]
            [:. :. :. :. :.]]))))

(run-tests)
