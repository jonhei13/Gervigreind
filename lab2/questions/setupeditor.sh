#!/bin/bash

if [[ -f ~/.editor-for-tsam ]]; then
	EDITOR="$(cat ~/.editor-for-tsam)"
fi

while ! which "$EDITOR" &>/dev/null; do
	cat <<QUESTION
Which editor do you want to use for answering questions?
Enter
	1 for nano (default)
	2 for vi
	3 for emacs
	or the name of your editor of choice (must be installed on skel).
QUESTION
	read answer
	case "$answer" in
		1)
        EDITOR=nano ;;
		2)
        EDITOR=vi ;;
		3)
        EDITOR=emacs ;;
        *)
        EDITOR="$answer" ;;
	esac
	if which "$EDITOR" &>/dev/null; then
		echo "$EDITOR" > ~/.editor-for-tsam
		break
	else
		echo "No executable named '$answer' found!"
	fi
done
export EDITOR
