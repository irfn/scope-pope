(defproject scope-pope "1.0.0-SNAPSHOT" 
	:description "FIXME: write" 
	:repositories [["central-proxy" "http://repository.sonatype.org/content/repositories/central/"]]
	:dependencies [[org.clojure/clojure "1.1.0-alpha-SNAPSHOT"] 
								 [org.clojure/clojure-contrib "1.0-SNAPSHOT"]
								 [commons-logging/commons-logging "1.1.1"]
                 [commons-collections/commons-collections "3.1"]
                 [opensymphony/quartz "1.6.3"]
								 [javax.mail/mail "1.4.1"]
								 [org.apache.commons/commons-email "1.2"]]

	:dev-dependencies [[org.clojars.robertpfeiffer/swank-clojure "1.1.0"]
										 [leiningen/lein-swank "1.1.0"]]
	:main scope-pope
	:namespaces jobs)