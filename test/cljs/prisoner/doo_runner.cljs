(ns prisoner.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [prisoner.core-test]
            [prisoner.player-test]))

(doo-tests 'prisoner.player-test)
