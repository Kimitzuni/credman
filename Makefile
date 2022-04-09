JAVAC			= /usr/bin/javac
JAVA			= /usr/bin/java

all: combo

combo: compile run

compile:
	$(JAVAC) credman.java

run:
	[ ! -f "credman.class" ] && $(JAVA) credman.java || $(JAVA) credman

clean:
	rm credman.class