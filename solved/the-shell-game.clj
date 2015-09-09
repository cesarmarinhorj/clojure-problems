;;SOLVED
;;http://www.codewars.com/kata/the-shell-game/clojure
;;
;;Description:
;;
;;"The Shell Game" involves three shells/cups/etc upturned on a playing surface, with a ball placed underneath one of them.
;;The shells are then rapidly swapped round, and the game involves trying to track the swaps and, once they are complete,
;;identifying the shell containing the ball.
;;
;;This is usually a con, but you can assume this particular game is fair...
;;
;;Your task is as follows. Given the shell that the ball starts under, and list of swaps, return the location of the ball at the end. All shells are indexed by the position they are in at the time.
;;
;;For example, given the starting position 0 and the swap sequence [(0, 1), (1, 2), (1, 0)]:
;;
;;The first swap moves the ball from 0 to 1
;;The second swap moves the ball from 1 to 2
;;The final swap doesn't affect the position of the ball.
;;
;;So
;;(= (find-the-ball 0 [[0 1] [2 1] [0 1]]) 2)
;;There aren't necessarily only three cups in this game, but there will be at least two. You can assume all swaps are valid, and involve two distinct indices.
;;Fundamentals

(ns the-shell-game
  (:require [clojure.test :refer :all]))

(defn move-once [current-position move]
  (let [[from to] move]
    (cond
      (= current-position to) from
      (= current-position from) to
      :else current-position)))

(defn find-the-ball [starting-position moves]
  (reduce move-once starting-position moves))

(find-the-ball 0 [[0, 1]])

(find-the-ball 1 [[0, 1]])

(find-the-ball 3 [[0, 1]])

(find-the-ball 0 [[0, 1], [1, 2], [1, 0]])
