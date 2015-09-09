;;SOLVED
(defn fib
  ([til]
   (cond (= til 0) []
         (= til 1) [0N]
         :else (fib (- til 2) [0N 1N])))
  ([til fib-seq]
   (if (= til 0) fib-seq
       (recur (- til 1)
              (conj fib-seq (apply + (take-last 2 fib-seq)))))))

(fib 3)
(fib 5)
(fib 10)
