(ns ephemeral_front.handler
  (:use compojure.core
        ring.middleware.params
        ring.middleware.multipart-params
        clojure.java.io
        hiccup.core
        ephemeral_front.data
        ephemeral_front.filesystem)
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


(defn build-job-anchor [job]
  [:li [:a {:href (str "jobs/" (:_id job))} (str (:_id job) ": " (:name job))]])

(defn show-job-index []
  (html
    [:p
     [:a {:href "/"} "Submit new job"]]
    [:ul
     (map #(build-job-anchor %) (get-all-jobs))]))

(defn show-job-status [id]
  (let [job (get-job id)]
    (html
      [:h3 (str "status for " id ": " (:status job))])))
  

(defn create-job [job-name file]
  (let [job-id (add-new-job job-name nil)]
    (save-job-data job-id file)))

;; route helpers ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn redirect-to [location]
  {:status 302
   :headers {"location" location}})


(defroutes app-routes
  (GET "/" [] 
       (upload-form))
  (POST "/jobs" [job-name job-data :as request] 
        (create-job job-name job-data)
        (redirect-to "jobs"))
  (GET "/jobs" []
       (show-job-index))
  (GET ["/jobs/:id" :id #"[0-9]+"] [id]
       (show-job-status (read-string id)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
