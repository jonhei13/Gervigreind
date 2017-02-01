#!/bin/bash

. ./setupeditor.sh

q=$(basename "$0" .sh | sed -e 's/^question//')
answerfile=../answers/answer$q.txt

echo "==========================================================================="

if [[ ! -f $answerfile ]] ; then
	cat > $answerfile <<QUESTION
$q. Assess the following blind search algorithms wrt. their completeness, 
optimality, space and time complexity in the given environment: 
Depth-First Search, Breadth-First Search, Uniform-Cost Search.
If one of the algorithms is not complete, how could you fix it?

Enter your answer below this line and save the file. Anything above the
line will be ignored for grading!
===========================================================================
QUESTION
fi

# to extract everything below the line:
# cat $answerfile | sed -n -e '/^===/,$p' | sed -n -e '2,$p'

yesno=y
if [[ `cat $answerfile | sed -n -e '/^===/,$p' | sed -n -e '2,$p' | wc -l` > 0 ]] ; then
	echo "Your previous answer to $q:"
	cat $answerfile
	echo "==========================================================================="
	echo "Do you want to edit the answer (y/n)?"
	read -n 1 yesno
fi
if [[ "$yesno" == "y" ]]; then
	"$EDITOR" $answerfile
fi
