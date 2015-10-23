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

(defn empty-at? [board row-pos col-pos]
  (= (get-in board [row-pos col-pos]) :.))

(defn next-turn [current-turn]
  (if (= current-turn :w) :b :w))

(defn move [board row-pos col-pos turn]
  {:board (if (empty-at? board row-pos col-pos)
            (assoc-in board [row-pos col-pos] turn)
            board)
   :turn (next-turn turn)})

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
