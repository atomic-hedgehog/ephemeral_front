(ns ephemeral_front.data
  (:require [monger.core :as mg]
            [monger.collection :as collection])
  (:import [com.mongodb MongoOptions ServerAddress])) 



(defn- connect []
  (mg/connect!)
  (mg/set-db! (mg/get-db "ephemeral")))

(defn- get-next-id []
  (:value (collection/find-and-modify "sequence" {:_id "jobs"} {"$inc" {:value 1}} :return-new true)))

(defn add-new-job [job-name job-data]
  (let [id (get-next-id)]
    (collection/insert "jobs" {:_id id 
                               :name job-name
                               :status "not started"})
    id))

(defn get-job [job-id]
  (collection/find-one-as-map "jobs" {:_id job-id}))

(defn get-all-jobs []
  (collection/find-maps "jobs"))

;;TODO is this idomatic for monger?
(connect)
