JAVAC=javac
JAVA=java
MODULE_PATH=/usr/share/openjfx/lib
MODULES=javafx.controls,javafx.fxml
MAIN_CLASS=UI

all: compile run

compile:
	$(JAVAC) --module-path $(MODULE_PATH) --add-modules $(MODULES) $(MAIN_CLASS).java

run:
	$(JAVA) --module-path $(MODULE_PATH) --add-modules $(MODULES) $(MAIN_CLASS)

clean:
	rm -f *.class

