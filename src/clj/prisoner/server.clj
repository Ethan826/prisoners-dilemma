(ns prisoner.server
  (:require [prisoner.handler :refer [app]]
            [environ.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

 (defn -main [& args]
   (let [port (Integer/parseInt (or (env :port) "8082"))]
     (run-jetty app {:port port :join? false})))
