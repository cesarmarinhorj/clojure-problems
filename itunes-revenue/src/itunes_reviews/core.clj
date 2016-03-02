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

(get-reviews "736683061" "77f6833d423a0b796830518891b462119600cb15" 0)

(defn feature-as-string [feature]
  (str (:date feature) "        " (:position feature) "        " (:section feature) "        \n"))

(spit "adr-1113.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2013-11-01" "2013-11-30"))))
(spit "adr-1213.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2013-12-01" "2013-12-31"))))
(spit "adr-0114.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-01-01" "2014-01-31"))))
(spit "adr-0214.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-02-01" "2014-02-28"))))
(spit "adr-0314.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-03-01" "2014-03-31"))))
(spit "adr-0414.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-04-01" "2014-04-30"))))
(spit "adr-0514.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-05-01" "2014-05-31"))))
(spit "adr-0614.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-06-01" "2014-06-30"))))
(spit "adr-0714.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-07-01" "2014-07-31"))))
(spit "adr-0814.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-08-01" "2014-08-31"))))
(spit "adr-0914.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-09-01" "2014-09-30"))))
(spit "adr-1014.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-10-01" "2014-10-31"))))
(spit "adr-1114.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-11-01" "2014-11-30"))))
(spit "adr-1214.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-12-01" "2014-12-31"))))
(spit "adr-0115.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-01-01" "2015-01-31"))))
(spit "adr-0215.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-02-01" "2015-02-28"))))
(spit "adr-0315.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-03-01" "2015-03-31"))))
(spit "adr-0415.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-04-01" "2015-04-30"))))
(spit "adr-0515.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-05-01" "2015-05-31"))))
(spit "adr-0615.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-06-01" "2015-06-30"))))
(spit "adr-0715.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-07-01" "2015-07-31"))))
(spit "adr-0815.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-08-01" "2015-08-31"))))
(spit "adr-0915.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-09-01" "2015-09-30"))))
(spit "adr-1015.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-10-01" "2015-10-31"))))
(spit "adr-1115.txt" (apply str (map feature-as-string (get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-11-01" "2015-11-30"))))


(spit "te-1113.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2013-11-01" "2013-11-30"))))
(spit "te-1213.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2013-12-01" "2013-12-31"))))
(spit "te-0114.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-01-01" "2014-01-31"))))
(spit "te-0214.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-02-01" "2014-02-28"))))
(spit "te-0314.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-03-01" "2014-03-31"))))
(spit "te-0414.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-04-01" "2014-04-30"))))
(spit "te-0514.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-05-01" "2014-05-31"))))
(spit "te-0614.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-06-01" "2014-06-30"))))
(spit "te-0714.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-07-01" "2014-07-31"))))
(spit "te-0814.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-08-01" "2014-08-31"))))
(spit "te-0914.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-09-01" "2014-09-30"))))
(spit "te-1014.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-10-01" "2014-10-31"))))
(spit "te-1114.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-11-01" "2014-11-30"))))
(spit "te-1214.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2014-12-01" "2014-12-31"))))
(spit "te-0115.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-01-01" "2015-01-31"))))
(spit "te-0215.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-02-01" "2015-02-28"))))
(spit "te-0315.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-03-01" "2015-03-31"))))
(spit "te-0415.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-04-01" "2015-04-30"))))
(spit "te-0515.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-05-01" "2015-05-31"))))
(spit "te-0615.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-06-01" "2015-06-30"))))
(spit "te-0715.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-07-01" "2015-07-31"))))
(spit "te-0815.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-08-01" "2015-08-31"))))
(spit "te-0915.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-09-01" "2015-09-30"))))
(spit "te-1015.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-10-01" "2015-10-31"))))
(spit "te-1115.txt" (apply str (map feature-as-string (get-features "908073488" "77f6833d423a0b796830518891b462119600cb15" 0 "2015-11-01" "2015-11-30"))))

(get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2013-12-01" "2013-12-31")
(get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2013-11-01" "2013-11-30")
(get-features "736683061" "77f6833d423a0b796830518891b462119600cb15" 0 "2013-12-01" "2013-12-31")

(spit "adr.txt" (apply str (map #(str (:text %) "\n\n")
                                   (get-reviews "736683061" "77f6833d423a0b796830518891b462119600cb15" 0))))


(spit "te.txt" (apply str (map #(str (:text %) "\n\n")
                                   (get-reviews "908073488" "77f6833d423a0b796830518891b462119600cb15" 0))))
