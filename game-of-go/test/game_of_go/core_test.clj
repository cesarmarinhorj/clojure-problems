(ns game-of-go.core-test
  (:require [clojure.test :refer :all]
            [game-of-go.core :refer :all]))

(defn future-move [board future-turn]
  (->> board
       (map-indexed #(to-row-col-pos % (.indexOf %2 future-turn)))
       (remove nil?)
       (first)))

(defn verify-board [board-before board-after turn-after]
  (let [[b-row-pos b-col-pos :as future-b-pos] (future-move board-before :B)
        [w-row-pos w-col-pos :as future-w-pos] (future-move board-before :W)
        board (remove-future-moves board-before)
        expected {:board board-after :turn turn-after }]
    (cond future-b-pos (is (= (move board b-row-pos b-col-pos :b) expected))
          future-w-pos (is (= (move board w-row-pos w-col-pos :w) expected))
          :else nil)))

(defn remove-future-moves [board]
  (into [] (map #(replace {:W :. :B :.} %) board)))

(deftest hello-world
  (testing "simple placement"
    (verify-board [[:W :. :. :. :.]
                   [:. :. :. :. :.]
                   [:. :. :. :. :.]
                   [:. :. :. :. :.]
                   [:. :. :. :. :.]]

                  [[:w :. :. :. :.]
                   [:. :. :. :. :.]
                   [:. :. :. :. :.]
                   [:. :. :. :. :.]
                   [:. :. :. :. :.]] :b))

  (testing "can't place at a location that already has a piece"
    (is (= (move [[:w :. :. :. :.]
                  [:. :. :. :. :.]
                  [:. :. :. :. :.]
                  [:. :. :. :. :.]
                  [:. :. :. :. :.]] 0 0 :b)

           {:board [[:w :. :. :. :.]
                    [:. :. :. :. :.]
                    [:. :. :. :. :.]
                    [:. :. :. :. :.]
                    [:. :. :. :. :.]]
            :turn :b}))))

(deftest eyes
  (testing "can't place in an eye of the opposite color"
    (verify-board [[:. :. :b :. :.]
                   [:. :b :W :b :.]
                   [:. :. :b :. :.]]

                  [[:. :. :b :. :.]
                   [:. :b :. :b :.]
                   [:. :. :b :. :.]] :w))

  (testing "placement in an eye of the same color is allowed"
    (verify-board [[:. :. :b :. :.]
                   [:. :b :B :b :.]
                   [:. :. :b :. :.]]

                  [[:. :. :b :. :.]
                   [:. :b :b :b :.]
                   [:. :. :b :. :.]] :w)))

(deftest captures
  (testing "simple capture"
    (verify-board [[:. :. :w :. :.]
                   [:. :w :b :W :.]
                   [:. :. :w :. :.]]

                  [[:. :. :w :. :.]
                   [:. :w :. :w :.]
                   [:. :. :w :. :.]] :b)))


(run-tests)
