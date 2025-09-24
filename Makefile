APP_NAME = TexWing

all:
	mkdir build
	javac -cp src -d build ./src/Main/Main.java ./src/Main/Gui.java
	jar cvfm dist/$(APP_NAME).jar manifest.txt -C build .
run:
	java -cp build Main.Main

run-jar:
	java -jar dist/$(APP_NAME).jar

macos:
	mkdir -p $(APP_NAME).app/Contents/MacOS
	mkdir -p $(APP_NAME).app/Contents/Resources

clean:
	rm -rf build dist $(APP_NAME).jar #* *~ bin/*
