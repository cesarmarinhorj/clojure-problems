(ns game-of-go.core
  (:require [clojure.repl :as repl]
            [clojure.data.json :as json]
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
