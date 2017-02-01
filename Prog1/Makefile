.PHONY: answers handin answers/group.txt

course:=$(shell cat .course)
hw:=$(notdir $(realpath $(dir $(firstword $(MAKEFILE_LIST)))))

answers:
	mkdir -p answers
	cd questions; ./questions.sh

handin: answers/group.txt
	@rutool handin -c $(course) -p $(hw) answers src dist build.xml
	@rutool check -c $(course) -p $(hw)

answers/group.txt:
	@ cd questions; ./check_group_file.sh
