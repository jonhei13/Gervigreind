#!/bin/bash

. ./setupeditor.sh

q=$(basename "$0" .sh | sed -e 's/^question//')
answerfile=../answers/answer$q.txt

echo "==========================================================================="

if [[ ! -f $answerfile ]] ; then
	cat > $answerfile <<QUESTION
$q. Estimate the size of the state space assuming the environment has width W, 
length L and D dirty spots. Explain your estimate!

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
