(ns jobs.EmailPope
  (:use [clojure.contrib.duck-streams :only [spit]])
	(:import (java.net URL URLEncoder))
  (:gen-class
   :implements [org.quartz.Job]))
 

(defn url-encode
  "Wrapper around java.net.URLEncoder returning a (UTF-8) URL encoded
representation of text."
  [text]
  (URLEncoder/encode text "UTF-8"))

(def *filters* "filters[]=[Type][is][Story+Card]&filters[]=[Type][is][Defect]&filters[]=[Type][is][Dev+Task]&style=grid&color_by=type&aggregate_type=sum&aggregate_property=tasking+estimate&tab=Active+Cards&rerank=&group_by=")

(def *query* "http://mingle.dcx.rackspace.com/projects/autohost/cards.xml?")

(defn query-url []
	(str *query* (url-encode *filters*)))

(defn get-for [url]
	(new org.apache.commons.httpclient.methods.GetMethod url))

(defn execute-method [method]
	(.executeMethod 
		(new org.apache.commons.httpclient.HttpClient) method)
	(.getResponseBodyAsString method))

(defn get-cards 
	[]
	(execute-method (get-for (query-url))))

(defn -execute
  [this context]
  (spit "cards.xml" (get-cards)))