#!/bin/bash

course=$(cat ../.course)
hw=$(basename $(dirname $(pwd)))
q=$(basename "$0" .sh | sed -e 's/^question//')

echo "==========================================================================="

cat <<QUESTION
$q  Implement the missing parts of the vacuum cleaner Java program such that it
encodes your agent function.

Make sure your code is in the $course/$hw/src/ folder on skel.
(Press Enter to continue.)
QUESTION
read answer

echo
echo "Trying to compile the code ..."
(cd ..; ant build 2>&1 | tee run.log)
(cd ..; ant clean &> /dev/null)
if grep -i -E "(error|failed)" ../run.log > /dev/null ; then
	echo "!!! Error detected !!!"
else
	echo "Code seems to compile ok."
fi
echo "(Press Enter to continue.)"
read answer
