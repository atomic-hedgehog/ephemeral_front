(ns ephemeral_front.filesystem
  (:use clojure.java.io)
  (:import java.io.File))

(def ephemeral-data-root "/ephemeral_jobs")

(defn- make-job-dir [job-id]
  (let [path (str ephemeral-data-root "/" job-id)]
    (.mkdir (File. path))
    path))

(defn save-job-data [job-id job-data-file]
  (let [path (make-job-dir job-id)]
    (copy (:tempfile job-data-file) (File. (str path "/" (:filename job-data-file))))))

