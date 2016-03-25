(ns prisoner.player)

(defn get-other-player [player-num]
  "Given :player1 or :player2, returns opposite"
  (if (= :player1 player-num)
    :player2
    (if (= :player2 player-num)
      :player1
      (throw Exception. "Invalid player number"))))

(defn get-prev-move-helper [results player-num]
  "Given the results and the player-num, returns a map with the keys :me and
   :them with each key mapped to the function first or last, where that function
   is appropriate to retrieving the player or opponent's move from a vector
   representing a game. Throws an error if player-num is invalid, returns nil
   if results is empty."
  (if (= 0 (count results))
    nil
    (if (= :player1 player-num)
      {:me first :them last}
      (if (= :player2 player-num)
        {:me last :them first}
        (throw Exception. "Invalid player number")))))

(defn get-my-prev-move [results player-num]
  "Given a vector of vectors of results and the player number :player1 or
   :player2, returns the player’s previous move. If the results vector
   is empty, returns nil."
  (:me (get-prev-move-helper results player-num)) (last results))

;; ERROR here occurs when Tit for Tat
(defn get-their-prev-move [results player-num]
  "Given a vector of vectors of results and the player number :player1 or
   :player2, returns the opposite player’s previous move. If the results vector
   is empty, returns nil."
  (:them (get-prev-move-helper results player-num)) (last results))

(defn all-cooperate [_ __]
  "Always cooperates, regardless of arguments."
  :cooperate)

(defn all-defect [_ __]
  "Always defects, regardless of arguments."
  :defect)

(defn random [_ __]
  "Returns a random move, regardless of arguments."
  (if (= 0 (rand-int 2))
    :cooperate
    :defect))

(defn tit-for-tat [results player-num]
  "Opens with cooperate, plays opponent's previous move thereafter."
  (if (= 0 (count results))
    :cooperate
    (first (get-their-prev-move results player-num))))

(defn grim-trigger [results player-num]
  "Plays cooperate until a single defect, then always defects."
  (let [func (if (= player-num :player1)
               second
               (if (= player-num :player2)
                 first
                 (throw Exception. "Invalid player number")))]
    ;; Hold :cooperate in accumulator, replace with :defect if other player ever
    ;; plays :defect
    (reduce #(if (= (func %2) :defect) :defect %) :cooperate results)))

(defn pavlov [results player-num]
  "Cooperates on the first move. If a reward or temptation payoff is received
   in the last round then repeats last choice, otherwise chooses the opposite
   choice."
  (if (=
       (get-my-prev-move results player-num)
       (get-their-prev-move results player-num))
    :cooperate
    :defect))

(def players {:all-cooperate {:fn all-cooperate :name "All Cooperate"}
              :all-defect {:fn all-defect :name "All Defect"}
              :random {:fn random :name "Random"}
              :tit-for-tat {:fn tit-for-tat :name "Tit for Tat"}
              :grim-trigger {:fn grim-trigger :name "Grim Trigger"}
              :pavlov {:fn pavlov :name "Pavlov"}})
