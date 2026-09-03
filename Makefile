JAVAC = javac
JAVA = java
PACKAGE = com.MutantBattle
PACKAGE_PATH = com/MutantBattle

.PHONY: build run start

build:
	cd src/main/java && $(JAVAC) $(PACKAGE_PATH)/*.java $(PACKAGE_PATH)/*/*.java

run: build
	cd src/main/java && $(JAVA) $(PACKAGE).Main

start: run
	@echo "Program completed!"