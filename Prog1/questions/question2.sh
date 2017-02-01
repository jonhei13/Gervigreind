#!/bin/bash

course=$(cat ../.course)
hw=$(basename $(dirname $(pwd)))
q=$(basename "$0" .sh | sed -e 's/^question//')

echo "==========================================================================="

cat <<QUESTION
$q. Implement the model.

Make sure your code is in the $course/$hw/src/ folder on skel.
(Press Enter to continue.)
QUESTION
read answer

echo
if [[ ! -r ../src/Main.java ]] ; then
	echo "ERROR: Can't find Main.java in src folder!"
else
	echo "Trying to compile the code ..."
	(cd ..; ant build 2>&1 | tee run.log)
	(cd ..; ant clean &> /dev/null)
	if grep -i -E "(error|failed)" ../run.log > /dev/null ; then
		echo "!!! Error detected !!!"
	else
		echo "Code seems to compile ok."
	fi
fi
echo "(Press Enter to continue.)"
read answer
