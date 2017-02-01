#!/bin/bash

course=$(cat ../.course)
hw=$(basename $(dirname $(pwd)))
q=$(basename "$0" .sh | sed -e 's/^question//')

echo "==========================================================================="

cat <<QUESTION
$q. Develop a model of the environment.
QUESTION
