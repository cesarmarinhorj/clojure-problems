(ns itunes-reviews.core
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

(defn headers [api-key]
  {:headers {:Authorization (str "Bearer " api-key)}})

(defn http-get-body [url headers]
  (:body (client/get url headers)))

(defn get-reviews [app-id api-key page-index]
  (let [url (review-url app-id page-index)
        body (http-get-body url (headers api-key))
        json-response (json/read-json body)]
    (:reviews json-response)))

(spit "page-0.txt" (get-reviews "736683061" "77f6833d423a0b796830518891b462119600cb15" 0))
(print (slurp "page-0.txt"))
