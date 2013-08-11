(ns ephemeral_front.handler
  (:use compojure.core
        hiccup.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defn upload-form []
    (html [:form {:action "/jobs" :method "post" :enctype "multipart/form-data"}
           [:p "Job Name" [:br]
            [:input {:name "job-name" :type "text"}]]
           [:p "Source Data File" [:br]
            [:input {:name "job-data" :size "30" :type "file"}]] 
           [:br]
           [:input {:type "submit" :name "submit" :value "Submit Job"}]]))

(defn create-job []
  (html [:h3 "Job created"]))

;; TODO refactor mock job data into a test framework
(def mock-job-data {:1 {:id :1 :name "Count the frequencies of words in Moby Dick"} 
                    :2 {:id :2 :name "Compile a list of emails containing the word 'vacation'"}})

(defn redirect-to [location]
  {:status 302
   :headers {"location" location}})

(defn build-job-anchor [job-id]
  [:li [:a {:href (str "jobs/" (:id (mock-job-data job-id)))} (:name (mock-job-data job-id))]])

(defn show-job-index []
  (html
    [:ul
     (map #(build-job-anchor (:id %)) (vals mock-job-data))]))

(defn show-job-status [id]
  (html
    [:h3 (str "status for " id)]))
  




(defroutes app-routes
  (GET "/" [] 
       (upload-form))
  (POST "/jobs" []
        (create-job)
        (redirect-to "/jobs"))
  (GET "/jobs" []
       (show-job-index))
  (GET "/jobs/:id" [id]
       (show-job-status id))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
