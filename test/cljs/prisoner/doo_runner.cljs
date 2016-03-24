(ns prisoner.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [prisoner.core-test]
            [prisoner.player-test]
            [prisoner.game-test]))

(doo-tests 'prisoner.core-test 'prisoner.player-test 'prisoner.game-test)
