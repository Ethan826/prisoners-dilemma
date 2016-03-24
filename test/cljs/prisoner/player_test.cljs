(ns prisoner.player-test
  (:require [cljs.test :refer-macros [is are deftest testing use-fixtures]]
            [prisoner.player :as p]))

(def empty-results [])
(def one-round-all-c [[:cooperate :cooperate]])
(def one-round-all-d [[:defect :defect]])
(def one-round-p1-d [[:defect :cooperate]])
(def one-round-p2-d [[:defect :cooperate]])

(def five-rounds-against-all-c [[:cooperate :cooperate]
                                [:defect :cooperate]
                                [:cooperate :cooperate]
                                [:defect :cooperate]
                                [:cooperate :cooperate]])

(def five-rounds-against-all-d [[:cooperate :defect]
                                [:defect :defect]
                                [:cooperate :defect]
                                [:defect :defect]
                                [:cooperate :defect]])

(def five-rounds-with-single-d [[:cooperate :cooperate]
                                [:cooperate :cooperate]
                                [:cooperate :defect]
                                [:cooperate :cooperate]
                                [:cooperate :cooperate]])

(def last-round-was-reward [[:cooperate :cooperate]])
(def last-round-was-temptation [[:defect :cooperate]])
(def last-round-was-punishment [[:defect :defect]])
(def last-round-was-sucker [[:cooperate :defect]])

(deftest all-cooperate-works
  (is (= :cooperate (p/all-cooperate [] :player1)))
  (is (= :cooperate (p/all-cooperate [[:cooperate :defect]])))
  (is (= :cooperate (p/all-cooperate [[:defect :cooperate]]))))

(deftest all-defect-works
  (is (= :defect (p/all-defect empty-results :player1)))
  (is (= :defect (p/all-defect one-round-all-c :player1)))
  (is (= :defect (p/all-defect five-rounds-against-all-c :player1))))

(deftest random-gives-results
  (is (let [r (p/random empty-results :player1)] (or (= r :cooperate) (= r :defect))))
  (is (let [r (p/random one-round-all-c :player1)] (or (= r :cooperate) (= r :defect))))
  (is (let [r (p/random five-rounds-against-all-c :player1)] (or (= r :cooperate) (= r :defect)))))

(deftest tit-for-tat-works
  (is (= :cooperate (p/tit-for-tat empty-results :player1)))
  (is (= :cooperate (p/tit-for-tat one-round-all-c :player1)))
  (is (= :defect (p/tit-for-tat one-round-all-d :player1)))
  (is (= :cooperate (p/tit-for-tat five-rounds-against-all-c :player1)))
  (is (= :defect (p/tit-for-tat five-rounds-against-all-d :player1))))

(deftest grim-trigger-works
  (is (= :cooperate (p/grim-trigger empty-results :player1)))
  (is (= :cooperate (p/grim-trigger one-round-all-c :player1)))
  (is (= :defect (p/grim-trigger one-round-all-d :player1)))
  (is (= :cooperate (p/grim-trigger five-rounds-against-all-c :player1)))
  (is (= :defect (p/grim-trigger five-rounds-against-all-d :player1)))
  (is (= :defect (p/grim-trigger five-rounds-with-single-d :player1))))

(deftest pavlov-works
  (is (= :cooperate (p/pavlov last-round-was-reward :player1)))
  (is (= :defect (p/pavlov last-round-was-temptation :player1)))
  (is (= :cooperate (p/pavlov last-round-was-punishment :player1)))
  (is (= :defect (p/pavlov last-round-was-sucker :player1))))
