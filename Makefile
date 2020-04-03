all:
	./gradlew :client:build
	./gradlew :server:jar
	cp server/build/libs/server.jar kraski_server.jar
	cp client/build/bundle/main.bundle.js main.bundle.js
	java -jar kraski_server.jar