(ns scope-pope
  (:import (org.quartz JobDetail SimpleTrigger)
           (org.quartz.impl StdSchedulerFactory)
           (jobs EmailPope)))
 
(def *scheduler* (atom nil))
 
(defn start-scheduler []
  (reset! *scheduler* (StdSchedulerFactory/getDefaultScheduler))
  (.start @*scheduler*))
 
(defn schedule-job
  [job when]
  (let [job (JobDetail. "job1" "group1" job)
        trigger (SimpleTrigger. "trigger1" "group1" when)]
    (.scheduleJob @*scheduler* job trigger)))
 
;; (start-scheduler)
;;(schedule-job EmailPope (java.util.Date.))