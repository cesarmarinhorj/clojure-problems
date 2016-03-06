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

(spit "anc-rank.txt" (get-ranks anc auth-token 0 "2013-01-01" "2016-12-31"))
(spit "adr-rank.txt" (get-ranks adr auth-token 0 "2013-01-01" "2016-12-31"))
(spit "te-rank.txt" (get-ranks te auth-token 0 "2013-01-01" "2016-12-31"))

(defn avg [numbers]
  (/ (apply + numbers) (count numbers)))

(defn revenue-for-date [date]
  (first (filter #(= (:date %) date) (read-string (slurp "app-revenue.txt")))))

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

(defn downloads-for-rank [rank]
  (let [adr-ranks (read-string (slurp "adr-rank.txt"))
        anc-ranks (read-string (slurp "anc-rank.txt"))
        te-ranks  (read-string (slurp "te-rank.txt"))]
        adr-ranks))


(rank-for-date-app :2016-01-01 :anc)
(read-string (slurp "anc-rank.txt"))
(read-string (slurp "adr-rank.txt"))
(read-string (slurp "te-rank.txt"))
(read-string (slurp "app-revenue.txt"))
(downloads-for-date-app :2015-01-01 :adr)
(downloads-for-date-app :2015-01-01 :te)
(downloads-for-date-app :2015-01-01 :te)
(revenue-for-date-app :2015-01-01 :te)
(revenue-for-date :2015-01-01)
