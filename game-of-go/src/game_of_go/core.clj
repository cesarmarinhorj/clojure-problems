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

(def turns {:w :b :b :w})

(defn empty-at? [board row-pos col-pos]
  (= (get-in board [row-pos col-pos]) :.))

(defn next-turn [current-turn]
  (current-turn turns))

(defn above [board row-pos col-pos]
  (get-in board [(dec row-pos) col-pos]))

(defn below [board row-pos col-pos]
  (get-in board [(inc row-pos) col-pos]))

(defn left [board row-pos col-pos]
  (get-in board [row-pos (dec col-pos)]))

(defn right [board row-pos col-pos]
  (get-in board [row-pos (inc col-pos)]))

(defn eye? [board row-pos col-pos turn]
  (= (above board row-pos col-pos)
     (below board row-pos col-pos)
     (left  board row-pos col-pos)
     (right board row-pos col-pos)
     (next-turn turn)))

(defn can-place? [board row-pos col-pos turn]
  (and (empty-at? board row-pos col-pos)
       (not (eye? board row-pos col-pos turn))))

(defn place [board row-pos col-pos turn]
  (assoc-in board [row-pos col-pos] turn))

(defn move [board row-pos col-pos turn]
  (if (can-place? board row-pos col-pos turn)

    {:board (place board row-pos col-pos turn)
     :turn  (next-turn turn)}

    {:board board
     :turn  turn}))

(defn to-row-col-pos [row-pos col-pos]
  (if (= col-pos -1)
    nil
    [row-pos col-pos]))

(defn future-move [board]
  (->> board
       (map-indexed #(to-row-col-pos % (.indexOf %2 :B)))
       (remove nil?)
       (first)))

(defn verify-board [from to]
  (future-move from))

(defn verify-board [board]
  (let [[row-pos col-pos] (future-move board)]
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

(run-tests)

;; (verify-board [[:. :. :b :. :.]
;;                [:. :b :w :B :.]
;;                [:. :. :b :. :.]])

;; (defn verify-board [from to])

;; (verify-board [[:. :. :b :. :.]
;;                [:. :b :w :B :.]
;;                [:. :. :b :. :.]]


;;               [[:. :. :b :. :.]
;;                [:. :b :. :b :.]
;;                [:. :. :b :. :.]])
