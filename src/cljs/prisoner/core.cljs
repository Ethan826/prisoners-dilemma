(ns prisoner.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [reagent-forms.core :refer [bind-fields]]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]))

;; -------------------------
;; Components

(def my-form
  [:div.row
   [:div.col-sm-8.col-md-offset-2
    [:div.form-group ; Same as row
     [:label "Player 1 strategy"]
     [:select.form-control {:type :list :id :p1}]]
    [:div.form-group ; Same as row
     [:label "Player 2 strategy"]
     [:select.form-control {:type :list :id :p2}]]
    [:div.form-group
     [:label "Number of rounds"]
     [:input.form-control {:field :numeric
                           :type :number
                           :id :rounds}]]]])

;; -------------------------
;; Views

(defn home-page []
  (let [doc (atom {:rounds 5000})]
    (fn []
      [:div.container-fluid
       [:div.col-sm-8.col-md-offset-2
        [:div.row
         [:h1 "Iterated Prisoner’s Dilemma Simulator"]]]
       [bind-fields my-form doc]
       [:div.col-sm-8.col-md-offset-2
        [:p (str @doc)]]])))

;; [:div [:a {:href "/about"} "go to about page"]]])

;; (defn about-page []
;;   [:div [:h2 "About prisoner"]
;;    [:div [:a {:href "/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

;; (secretary/defroute "/about" []
;;   (session/put! :current-page #'about-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (secretary/dispatch! path))
    :path-exists?
    (fn [path]
      (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))

(enable-console-print!)
