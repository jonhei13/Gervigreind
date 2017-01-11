#!/bin/bash

. ./setupeditor.sh

q=$(basename "$0" .sh | sed -e 's/^question//')
answerfile=../answers/answer$q.txt

echo "==========================================================================="

if [[ ! -f $answerfile ]] ; then
	cat > $answerfile <<QUESTION
$q. For each of the three given environments, record the number of steps taken
and the score returned by the simulator.

Enter your answer below this line and save the file. Anything above the
line will be ignored for grading!
===========================================================================
vacuumcleaner: steps = , score =
vacuumcleaner_random: steps = , score =
vacuumcleaner_random_big: steps = , score =
QUESTION
fi

# to extract everything below the line:
# cat $answerfile | sed -n -e '/^===/,$p' | sed -n -e '2,$p'

yesno=y
if [[ `cat $answerfile | sed -n -e '/^===/,$p' | sed -n -e '2,$p' | grep -v 'vacuumcleaner: steps = , score =' | grep -v 'vacuumcleaner_random: steps = , score =' | grep -v 'vacuumcleaner_random_big: steps = , score =' | wc -l` > 0 ]] ; then
	echo "Your previous answer to $q:"
	cat $answerfile
	echo "==========================================================================="
	echo "Do you want to edit the answer (y/n)?"
	read -n 1 yesno
fi
if [[ "$yesno" == "y" ]]; then
	"$EDITOR" $answerfile
fi
