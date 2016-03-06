(ns itunes-revenue.core
  (:require [clj-http.client :as client]
            [clojure.repl :as repl]
            [clojure.data.json :as json]
            [clojure.java.io :as io]))

(def api-url "https://api.appannie.com/v1.2/apps/ios/app/")

(defn review-url [app-id page-index]
  (str api-url
       app-id
       "/reviews?&countries=US&start_date=2013-11-01&page_index="
       page-index))

(defn features-url [app-id page-index start-date end-date]
  (str api-url
       app-id
       "/features?countries=US"
       "&start_date=" start-date
       "&end_date=" end-date
       "&page_index=" page-index))

(defn ranks-url [app-id page-index start-date end-date]
  (str api-url
       app-id
       "/ranks?interval=daily&countries=US"
       "&start_date=" start-date
       "&end_date=" end-date
       "&page_index=" page-index))

(defn headers [api-key]
  {:headers {:Authorization (str "Bearer " api-key)}})

(defn http-get-body [url headers]
  (:body (client/get url headers)))

(defn get-reviews [app-id api-key page-index]
  (let [url (review-url app-id page-index)
        body (http-get-body url (headers api-key))
        json-response (json/read-json body)]
    (:reviews json-response)))

(defn get-features [app-id api-key page-index start-date end-date]
  (let [url (features-url app-id page-index start-date end-date)
        body (http-get-body url (headers api-key))
        json-response (json/read-json body)]
    (:features json-response)))

(defn paid-feed? [rank-entry]
  (and (= (:category rank-entry) "Overall > Games")
       (= (:feed rank-entry) "paid")))

(defn to-date-record [hash]
    (into {} [[:date (first hash)] [:rank (last hash)]]))

(defn get-ranks [app-id api-key page-index start-date end-date]
  (let [url (ranks-url app-id page-index start-date end-date)
        body (http-get-body url (headers api-key))
        json-response (json/read-json body)
        product-ranks (:product_ranks json-response)
        paid-feed (first (filter paid-feed? product-ranks))]
    (sort-by #(:date %)
             (into [] (map to-date-record (:ranks paid-feed))))))

(def adr "736683061")
(def anc "977865620")
(def te "908073488")
(def auth-token "77f6833d423a0b796830518891b462119600cb15")

(defn avg [numbers]
  (if (= 0 (count numbers)) nil (/ (apply + numbers) (count numbers))))

(defn revenue-for-date [date]
  (or (first (filter #(= (:date %) date) (read-string (slurp "app-revenue.txt")))) { :date date :revenue [0 0 0] }))

(defn revenue-for-date-app [date app]
  (let [revenue (:revenue (revenue-for-date date))]
    (case app
      :anc (nth revenue 0)
      :te  (nth revenue 1)
      :adr (nth revenue 2))))

(defn downloads-for-date-app [date app]
  (int (/ (revenue-for-date-app date app) 0.7)))

(defn rank-from-file [date file]
  (:rank (first (filter #(= (:date %) date) (read-string (slurp file))))))

(defn rank-for-date-app [date app]
  (case app
    :anc (rank-from-file date "anc-rank.txt")
    :te (rank-from-file date "te-rank.txt")
    :adr (rank-from-file date "adr-rank.txt")))

(defn include? [target collection]
  (some #(= target %) collection))

(defn lowest-rank [min-value max-value collection]
  (first (sort-by #(* -1 %) (filter #(and (> % min-value) (< % max-value)) collection))))

(defn rank-close-to [target ranks]
  (or (and (include? target ranks) target)
      (lowest-rank (- target 10) (+ target 10) ranks)
      (lowest-rank (- target 20) (+ target 20) ranks)
      (lowest-rank (- target 30) (+ target 30) ranks)
      (lowest-rank (- target 40) (+ target 40) ranks)
      (lowest-rank (- target 50) (+ target 50) ranks)
      (lowest-rank (- target 60) (+ target 60) ranks)
      (lowest-rank (- target 70) (+ target 70) ranks)
      (lowest-rank (- target 80) (+ target 80) ranks)
      (lowest-rank (- target 90) (+ target 90) ranks)
      (lowest-rank (- target 100) (+ target 100) ranks)

      (lowest-rank (- target 110) (+ target 110) ranks)
      (lowest-rank (- target 120) (+ target 120) ranks)
      (lowest-rank (- target 130) (+ target 130) ranks)
      (lowest-rank (- target 140) (+ target 140) ranks)
      (lowest-rank (- target 150) (+ target 150) ranks)
      (lowest-rank (- target 160) (+ target 160) ranks)
      (lowest-rank (- target 170) (+ target 170) ranks)
      (lowest-rank (- target 180) (+ target 180) ranks)
      (lowest-rank (- target 190) (+ target 190) ranks)
      (lowest-rank (- target 200) (+ target 200) ranks)

      (lowest-rank (- target 210) (+ target 210) ranks)
      (lowest-rank (- target 220) (+ target 220) ranks)
      (lowest-rank (- target 230) (+ target 230) ranks)
      (lowest-rank (- target 240) (+ target 240) ranks)
      (lowest-rank (- target 250) (+ target 250) ranks)
      (lowest-rank (- target 260) (+ target 260) ranks)
      (lowest-rank (- target 270) (+ target 270) ranks)
      (lowest-rank (- target 280) (+ target 280) ranks)
      (lowest-rank (- target 290) (+ target 290) ranks)
      (lowest-rank (- target 300) (+ target 300) ranks)

      (lowest-rank (- target 410) (+ target 410) ranks)
      (lowest-rank (- target 420) (+ target 420) ranks)
      (lowest-rank (- target 430) (+ target 430) ranks)
      (lowest-rank (- target 440) (+ target 440) ranks)
      (lowest-rank (- target 450) (+ target 450) ranks)
      (lowest-rank (- target 460) (+ target 460) ranks)
      (lowest-rank (- target 470) (+ target 470) ranks)
      (lowest-rank (- target 480) (+ target 480) ranks)
      (lowest-rank (- target 490) (+ target 490) ranks)
      (lowest-rank (- target 500) (+ target 500) ranks)

      (lowest-rank (- target 510) (+ target 510) ranks)
      (lowest-rank (- target 520) (+ target 520) ranks)
      (lowest-rank (- target 530) (+ target 530) ranks)
      (lowest-rank (- target 540) (+ target 540) ranks)
      (lowest-rank (- target 550) (+ target 550) ranks)
      (lowest-rank (- target 560) (+ target 560) ranks)
      (lowest-rank (- target 570) (+ target 570) ranks)
      (lowest-rank (- target 580) (+ target 580) ranks)
      (lowest-rank (- target 590) (+ target 590) ranks)
      (lowest-rank (- target 600) (+ target 600) ranks)

      (lowest-rank (- target 610) (+ target 610) ranks)
      (lowest-rank (- target 620) (+ target 620) ranks)
      (lowest-rank (- target 630) (+ target 630) ranks)
      (lowest-rank (- target 640) (+ target 640) ranks)
      (lowest-rank (- target 650) (+ target 650) ranks)
      (lowest-rank (- target 660) (+ target 660) ranks)
      (lowest-rank (- target 670) (+ target 670) ranks)
      (lowest-rank (- target 680) (+ target 680) ranks)
      (lowest-rank (- target 690) (+ target 690) ranks)
      (lowest-rank (- target 700) (+ target 700) ranks)

      (lowest-rank (- target 710) (+ target 710) ranks)
      (lowest-rank (- target 720) (+ target 720) ranks)
      (lowest-rank (- target 730) (+ target 730) ranks)
      (lowest-rank (- target 740) (+ target 740) ranks)
      (lowest-rank (- target 750) (+ target 750) ranks)
      (lowest-rank (- target 760) (+ target 760) ranks)
      (lowest-rank (- target 770) (+ target 770) ranks)
      (lowest-rank (- target 780) (+ target 780) ranks)
      (lowest-rank (- target 790) (+ target 790) ranks)
      (lowest-rank (- target 800) (+ target 800) ranks)

      (lowest-rank (- target 910) (+ target 910) ranks)
      (lowest-rank (- target 920) (+ target 920) ranks)
      (lowest-rank (- target 930) (+ target 930) ranks)
      (lowest-rank (- target 940) (+ target 940) ranks)
      (lowest-rank (- target 950) (+ target 950) ranks)
      (lowest-rank (- target 960) (+ target 960) ranks)
      (lowest-rank (- target 970) (+ target 970) ranks)
      (lowest-rank (- target 980) (+ target 980) ranks)
      (lowest-rank (- target 990) (+ target 990) ranks)
      (lowest-rank (- target 1000) (+ target 1000) ranks)
      (lowest-rank (- target 100000000000) (+ target 10000000000000000) ranks))))

(def adr-ranks (read-string (slurp "adr-rank.txt")))
(def anc-ranks (read-string (slurp "anc-rank.txt")))
(def te-ranks (read-string (slurp "te-rank.txt")))

(defn closest-rank-by-app [rank]
  (let [closest-adr (rank-close-to rank (distinct (map :rank adr-ranks)))
        closest-anc (rank-close-to rank (distinct (map :rank anc-ranks)))
        closest-te (rank-close-to rank  (distinct (map :rank te-ranks)))]
    {:adr closest-adr :te closest-te :anc closest-anc}))

(defn downloads-for-rank-app [rank app rank-data]
  (let [dates (map :date (filter #(= (:rank %) rank) rank-data))]
    (if (= 0 (count dates)) nil
        (int (avg (map #(downloads-for-date-app % app) dates))))))

(defn downloads-for-rank-by-app [rank-by-app]
  {:adr (downloads-for-rank-app (:adr rank-by-app) :adr adr-ranks)
   :te  (downloads-for-rank-app (:te rank-by-app) :te te-ranks)
   :anc (downloads-for-rank-app (:anc rank-by-app) :anc anc-ranks)})

(defn downloads-for-rank [rank]
  (let [closest (closest-rank-by-app rank)
        downloads (downloads-for-rank-by-app closest)]
    (int (avg [(:anc downloads) (:te downloads)]))))

(defn get-lifetime-downloads [app-id]
  (println "calculating....")
  (println (reduce + (map downloads-for-rank (map :rank (get-ranks app-id auth-token 0 "2013-01-01" "2016-12-31"))))))

(get-lifetime-downloads "902062673")
(get-lifetime-downloads "736683061")

(downloads-for-rank 300)

;; (closest-rank-by-app 822)
;; (downloads-for-rank-app 830 :adr adr-ranks)

;; (downloads-for-rank-by-app {:adr 830, :te 823, :anc 822})

;; (map :date (filter #(= (:rank %) 830) adr-ranks))

;; (downloads-for-date-app :2013-11-26 :adr)

;; (revenue-for-date :2013-11-26)


;; (downloads-for-rank 822)
;; (downloads-for-rank-app 150 :te te-ranks)
;; (downloads-for-rank-app 150 :anc anc-ranks)

;; (rank-close-to 15 [1 2 3 4 5 6 7 8 9 10 40])
;; (some #(= 2 %) [1 2])
;; (spit "anc-rank.txt" )
;; (spit "adr-rank.txt" (get-ranks adr auth-token 0 "2013-01-01" "2016-12-31"))
;; (spit "te-rank.txt" (get-ranks te auth-token 0 "2013-01-01" "2016-12-31"))
;; (rank-for-date-app :2016-01-01 :anc)
;; (read-string (slurp "anc-rank.txt"))
;; (read-string (slurp "adr-rank.txt"))
;; (read-string (slurp "te-rank.txt"))
;; (read-string (slurp "app-revenue.txt"))
;; (downloads-for-date-app :2015-01-01 :adr)
;; (downloads-for-date-app :2015-01-01 :te)
;; (downloads-for-date-app :2015-01-01 :te)
;; (revenue-for-date-app :2015-01-01 :te)
;; (revenue-for-date :2015-01-01)
