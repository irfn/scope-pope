(ns jobs.EmailPope
	(:require [clojure.zip :as zip]
            [clojure.xml :as xml]
            [clojure.contrib.zip-filter])
 (:use clojure.contrib.zip-filter.xml
       clojure.contrib.duck-streams)
	(:import (java.net URL URLEncoder)
					 (java.io ByteArrayInputStream)
					 (org.apache.commons.mail SimpleEmail))
 (:gen-class
  :implements [org.quartz.Job]))
 

(defn url-encode
  "Wrapper around java.net.URLEncoder returning a (UTF-8) URL encoded representation of text."
  [text]
  (URLEncoder/encode text "UTF-8"))

(def *filters* "SELECT COUNT(*) WHERE Type = 'Story Card'")

(def *project-url* "http://mingle.dcx.rackspace.com/projects/autohost")

(def *query* "/cards/execute_mql.xml?mql=")

(defn query-url
	[]
	(str *project-url* *query* (url-encode *filters*)))

(defn results 
	[]
	(zip/xml-zip (clojure.xml/parse (query-url))))

(defn result
	[]
	(first (get (first
							 (xml1-> (results) :results :result :Count-))
							:content)))

(defn update-and-report [scope]	
	(doto (SimpleEmail.)
      (.setHostName "smtp.gmail.com")
      (.setSslSmtpPort "465")
      (.setSSL true)
      (.addTo "ishah@thoughtworks.com")
      (.setFrom "pope@irfanshah.net" "Scope Pope")
      (.setSubject "Scope changed")
      (.setMsg (str "Scope changed from " (slurp "scope.results") " to " scope))
      (.setAuthentication "pope@irfanshah.net" "!abcd1234")
      (.send))
	(spit "scope.results" scope))
	
(defn -execute
  [this context]
	(let [scope-results (result)]
		(if (not (= scope-results
								(slurp "scope.results")))
			(update-and-report scope-results))))