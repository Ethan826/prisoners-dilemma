(ns prisoner.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [prisoner.game :as g]
            [prisoner.player :as p]
            [reagent-forms.core :refer [bind-fields]]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]))

;; -------------------------
;; State

(def doc (atom {:rounds 500
                :results nil}))

;; -------------------------
;; Validation

(defn validate-rounds [val] (= (type val) js/Number))

(defn enforce-number [id value local-copy-of-doc master-copy-of-doc]
  (if (keyword-identical? (first id) :rounds)
    (if (validate-rounds value)
      (assoc @master-copy-of-doc :rounds value)
      @master-copy-of-doc)
    nil))
;; -------------------------
;; Components and functions

(def my-form
  [:div.row
   [:div.col-sm-8.col-md-offset-2
    [:div.form-group
     [:label "Player 1 strategy"]
     [:select.form-control {:field :list :id :p1}
      (for [strategy (keys p/players)]
        [:option {:key strategy} (-> p/players strategy :name)])]]
    [:div.form-group
     [:label "Player 2 strategy"]
     [:select.form-control {:field :list :id :p2}
      (for [strategy (keys p/players)]
        [:option {:key strategy} (-> p/players strategy :name)])]]
    [:div.form-group
     [:label "Number of rounds"]
     [:input.form-control
      {:field :numeric
       :type :number
       :id :rounds}]]]])

(defn run-simulation [d]
  (assoc @d :results
         (g/get-score
          (g/run-series
           (-> @d :p1 p/players :fn)
           (-> @d :p2 p/players :fn)
           (:rounds @d)))))

;; -------------------------
;; Views

(defn home-page []
  [:div.container-fluid
   [:div.row
    [:div.col-sm-8.col-md-offset-2
     [:h1 "Iterated Prisoner’s Dilemma Simulator"]]]
   [bind-fields
    my-form
    doc
    (fn [id value local-copy-of-doc]
      (enforce-number id value local-copy-of-doc doc))] ; Keep helper function pure
   [:div.row
    [:div.col-sm-8.col-md-offset-2
     [:button.btn.btn-primary
      {:on-click (fn []
                   (reset! doc (run-simulation doc)))} ; Keep helper function pure
      "Submit"]]]
   [:div.col-sm-8.col-md-offset-2
    (let [results (:results @doc)
          p1-score (first (:results @doc))
          p2-score (second (:results @doc))]
      (if results
        [:div
         [:hr]
         [:h3 "Results"]
         [:table.table-condensed.table-responsive
          [:tr
           [:td [:strong "Player 1"]]
           [:td p1-score]
           (if (< p1-score p2-score) [:td [:strong "Winner"]])]
          [:tr
           [:td [:strong "Player 2"]]
           [:td p2-score]
           (if (< p2-score p1-score) [:td [:strong "Winner"]])]]]))]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

;; -------------------------
;; Initialize app

(defn init! []
  (reagent/render [home-page] (.getElementById js/document "app")))

(enable-console-print!)
