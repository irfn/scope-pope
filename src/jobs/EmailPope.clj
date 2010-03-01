(ns jobs.EmailPope
	(:require [clojure.zip :as zip]
            [clojure.xml :as xml]
            [clojure.contrib.zip-filter]
 						[clojure.http.client :as http])
	(:use [clojure.contrib.zip-filter.xml]
				[clojure.contrib.duck-streams])
	
	(:import (java.net URL URLEncoder)
					 (java.io ByteArrayInputStream)
					 (org.apache.commons.mail SimpleEmail))
 (:gen-class
  :implements [org.quartz.Job]))
 

(defn url-encode
  "Wrapper around java.net.URLEncoder returning a (UTF-8) URL encoded representation of text."
  [text]
  (URLEncoder/encode text "UTF-8"))

(def *filters* "SELECT SUM('Release Estimate') WHERE type = Story AND Release  = '1 - Interim Release' AND 'Story Type' != 'UI' AND Status != 'Dropped'  AND 'Scope Increase' != 'TW' AND Iteration IS NOT NULL")

(def *project-url* "https://anandv:abcd1234@minglehosting.thoughtworks.com/bcg/api/v2/projects/bcg_contact_management_phase_a")

(def *query* "/cards/execute_mql.xml?mql=")

(defn query-url
	[]
	(str *project-url* *query* (url-encode *filters*)))

(defn results []
	(ByteArrayInputStream. (.getBytes (nth (:body-seq (http/request (query-url))) 3))))

(defn result
	[]
	(first (get (clojure.xml/parse (results)) :content)))

(defn update-and-report [scope]	
	(doto (SimpleEmail.)
      (.setHostName "smtp.gmail.com")
      (.setSslSmtpPort "465")
      (.setSSL true)
      (.addTo "anandv@thoughtworks.com")
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