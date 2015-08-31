;;Order
;;{ :date {:year 2012 :month 12 :day 31} }

;;Customer
;;{ :name "foo"
;;  :orders [{ :date {:year 2012 :month 12 :day 31) }
;;           { :date {:year 2012 :month  1 :day  1) }]}

(defn order [year month day]
  {:date {:year year :month month :day day}})

(defn year-group [order] (:year (:date order)))

(def customer-list [{:name "foo"
                     :orders [(order 2012 12 31)
                              (order 2013  1  1)]}])
customer-list

(group-by year-group customer-list)

(group-by year-group (map :orders customer-list))

(map :orders customer-list)
