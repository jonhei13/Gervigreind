#!/bin/bash

course=$(cat ../.course)
hw=$(basename $(dirname $(pwd)))
q=$(basename "$0" .sh | sed -e 's/^question//')

echo "==========================================================================="

cat <<QUESTION
$q. Post the results (number of state expansions, cost of the found solution,
search time) on Piazza, to see how well you are doing compared to the other students.
QUESTION
