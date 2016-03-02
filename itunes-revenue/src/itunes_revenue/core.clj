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

(get-reviews "736683061" "77f6833d423a0b796830518891b462119600cb15" 0)
(get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2013-11-01" "2013-11-30")
(get-ranks "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-01-01" "2014-12-31")
