(ns game-of-go.core-test
  (:require [clojure.test :refer :all]
            [game-of-go.core :refer :all]))

(defn future-move [board future-turn]
  (->> board
       (map-indexed #(to-row-col-pos % (.indexOf %2 future-turn)))
       (remove nil?)
       (first)))

(defn verify-board [board]
  (let [[row-pos col-pos] (or (future-move board :B) (future-move board :W))]
    [row-pos col-pos]))

(deftest hello-world
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
            [:. :. :. :. :.]])))

  (testing "turn remains unchanged if invalid placement"
    (is (= (:turn (move [[:b :. :. :.]] 0 0 :w)) :w))))

(deftest eyes
  (testing "can't place in an eye of the opposite color"
    (is (= (move [[:. :. :b :. :.]
                  [:. :b :. :b :.]
                  [:. :. :b :. :.]] 1 2 :w)

           {:board [[:. :. :b :. :.]
                    [:. :b :. :b :.]
                    [:. :. :b :. :.]]
            :turn :w})))

  (testing "can place in an eye of the same color"
    (is (= (move [[:. :. :b :. :.]
                  [:. :b :. :b :.]
                  [:. :. :b :. :.]] 1 2 :b)

           {:board [[:. :. :b :. :.]
                    [:. :b :b :b :.]
                    [:. :. :b :. :.]]
            :turn :w}))))

(deftest captures
  (testing "simple capture"
    (is (= (move [[:. :. :w :. :.]
                  [:. :w :b :. :.]
                  [:. :. :w :. :.]] 1 3 :w)

           {:board [[:. :. :w :. :.]
                    [:. :w :. :w :.]
                    [:. :. :w :. :.]]
            :turn :b}))))

(verify-board [[:. :. :b :. :.]
               [:. :b :w :B :.]
               [:. :. :b :. :.]])


(run-tests)

(future-move [[:. :. :b :. :.]
              [:. :b :w :B :.]
              [:. :. :b :. :.]] :B)
