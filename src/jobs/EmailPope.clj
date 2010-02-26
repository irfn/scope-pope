(ns jobs.EmailPope
	(:require [clojure.zip :as zip]
            [clojure.xml :as xml]
            [clojure.contrib.zip-filter])
  (:use clojure.contrib.zip-filter.xml
        clojure.contrib.duck-streams)
	(:import (java.net URL URLEncoder)
					 (java.io ByteArrayInputStream))
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

(defn -execute
  [this context]
	(let scope-results (result))
	(if (not (= scope-results
				 (slurp "scope.results")))
		(spit "scope.results" (result)))
