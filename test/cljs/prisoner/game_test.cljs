(ns prisoner.game-test
  (:require [prisoner.game :as sut]
            [prisoner.player :as p]
            [cljs.test :refer-macros [is testing deftest]]))

(testing "run-game"
  (deftest run-game-works-with-empty
    (is (=
         [[:cooperate :cooperate]]
         (sut/run-game p/all-cooperate p/all-cooperate []))))
  (deftest run-game-works-with-results
    (is (=
         [[:cooperate :cooperate]
          [:cooperate :cooperate]]
         (sut/run-game p/all-cooperate p/all-cooperate [[:cooperate :cooperate]])))))

(testing "run-series"
  (deftest can-run-two-games
    (is (=
         (sut/run-series p/all-cooperate p/all-cooperate 2)
         [[:cooperate :cooperate]
          [:cooperate :cooperate]])))
  (deftest can-run-many-games
    (is (= 500 (count (sut/run-series p/random p/all-cooperate 500))))))

(deftest score-game-works
  (is (= (sut/score-game [:defect :cooperate]) [sut/temptation sut/sucker])))

(testing "get-score"
  (deftest get-score-empty
    (is (= (sut/get-score []) [0 0])))
  (deftest get-score-one-round
    (is (= (sut/get-score [[:cooperate :defect]]) [sut/sucker sut/temptation]))
    (is (= (sut/get-score [[:cooperate :cooperate]]) [sut/reward sut/reward]))
    (is (= (sut/get-score [[:defect :cooperate]]) [sut/temptation sut/sucker]))
    (is (= (sut/get-score [[:defect :defect]]) [sut/punishment sut/punishment])))
  (deftest get-score-multiple-rounds
    (is (= (sut/get-score
            (sut/run-series p/all-cooperate p/all-cooperate 50))
           [(* 50 sut/reward) (* 50 sut/reward)]))))

