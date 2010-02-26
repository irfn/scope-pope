(ns scope-pope
  (:import (org.quartz JobDetail CronTrigger)
           (org.quartz.impl StdSchedulerFactory)
					 (jobs EmailPope)))
 
(def *scheduler* (atom nil))
 
(defn start-scheduler []
  (reset! *scheduler* (StdSchedulerFactory/getDefaultScheduler))
  (.start @*scheduler*))

(defn schedule-job
  [job]
  (let [job (JobDetail. "job1" "group1" job)
        trigger (CronTrigger. "trigger1" "group1" "job1" "group1" "0 0/3 * ? * MON-FRI")]
    (.scheduleJob @*scheduler* job trigger)))
 
(start-scheduler)
(schedule-job EmailPope)