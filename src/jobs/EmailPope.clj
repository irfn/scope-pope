(ns jobs.EmailPope
  (:use [clojure.contrib.duck-streams :only [spit]])
  (:gen-class
   :implements [org.quartz.Job]))
 
(defn -execute
  [this context]
  (spit "quartz-example-job.txt" "Hello from the example job!"))