#!/bin/bash

q=$(basename "$0" .sh | sed -e 's/^question//')

echo "==========================================================================="

cat <<QUESTION
$q Implement a hash function for State objects that passes all the tests.
Make sure your code is in the right folder on skel.
(Press Enter to continue.)
QUESTION
read answer

echo
echo "Trying to run the code ..."
(cd ..; ant run 2>&1 | tee run.log)
(cd ..; ant clean &> /dev/null)
if grep -i -E "(error|failed)" ../run.log > /dev/null ; then
	echo "!!! Error detected !!!"
else
	echo "Code seems to compile and run ok."
	read answer
fi
echo "(Press Enter to continue.)"
read answer
