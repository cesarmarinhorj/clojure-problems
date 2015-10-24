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

(defn eye? [board row-pos col-pos turn]
  (let [above (get-in board [(dec row-pos) col-pos])
        below (get-in board [(inc row-pos) col-pos])
        left (get-in board [row-pos (dec col-pos)])
        right (get-in board [row-pos (inc col-pos)])]
    (= above below left right (next-turn turn))))


(defn move [board row-pos col-pos turn]
  (if (and (empty-at? board row-pos col-pos) (not (eye? board row-pos col-pos turn)))
    {:board (assoc-in board [row-pos col-pos] turn)
     :turn (next-turn turn)}
    {:board board :turn turn}))

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

(run-tests)
