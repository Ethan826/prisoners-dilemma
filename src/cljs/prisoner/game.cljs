(ns prisoner.game)

(def temptation 0)
(def reward 0.5)
(def punishment 6)
(def sucker 10)

(defn run-game [p1 p2 results]
  (conj results [(p1 results :player1) (p2 results :player2)]))

(defn run-series [p1 p2 rounds]
  (let [results []]
    (loop [results results
           rounds rounds]
      (if (= 0 rounds)
        results
        (recur (run-game p1 p2 results) (dec rounds))))))

(defn score-game [result]
  (case result
    [:cooperate :cooperate] [reward reward]
    [:cooperate :defect] [sucker temptation]
    [:defect :cooperate] [temptation sucker]
    [:defect :defect] [punishment punishment]))

(defn get-score [results]
  (reduce #(map + (score-game %2) %) [0 0] results))
