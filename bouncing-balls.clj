;;http://www.codewars.com/kata/5544c7a5cb454edb3c000047/train/clojure
;;
;;A child plays with a ball on the nth floor of a big building the height of which is known
;;
;;(float parameter "h" in meters, h > 0) .
;;
;;He lets out the ball. The ball rebounds for example to two-thirds
;;
;;(float parameter "bounce", 0 < bounce < 1)
;;
;;of its height.
;;
;;His mother looks out of a window that is 1.5 meters from the ground
;;
;;(float parameters window < h).
;;
;;How many times will the mother see the ball either falling or bouncing in front of the window
;;
;;(return a positive integer unless conditions are not fulfilled in which case return -1) ?
;;
;;Note
;;
;;You will admit that the ball can only be seen if the height of the rebouncing ball is stricty greater than the window parameter.
;;
;;Example:
;;
;;h = 3, bounce = 0.66, window = 1.5, result is 3
;;
;;h = 3, bounce = 1, window = 1.5, result is -1

(ns bouncing-balls.core)

(defn bouncing-balls [h bounce window]
  (if (>= window h)
    -1
    (loop [current (next-bounce h bounce) bounces 0]
      (if (> current window)
        (recur (next-bounce current bounce) (+' bounces 2))
        (+' bounces 1)))))


(defn next-bounce [h bounce] (*' h bounce))

(bouncing-balls 10 1 10)

(next-bounce 3 0.66)

;;(ns bouncing-balls.core-test
;;  (:require [clojure.test :refer :all]
;;            [bouncing-balls.core :refer :all]))
;;
;;(deftest a-test1
;;  (testing "Test 1"
;;    (def rr 3)
;;    (is (= (bouncing-balls 3 0.66 1.5) rr))))
;;
;;(deftest a-test2
;;  (testing "Test 2"
;;    (def rr 15)
;;        (is (= (bouncing-balls 30 0.66 1.5) rr))))
