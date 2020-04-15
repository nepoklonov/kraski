all:
	./gradlew run

website:
	./gradlew :server:jar
	./gradlew :client:build
	scp server/build/libs/server.jar root@46.254.19.56:/var/www/duma/kraski/kraski_server.jar
	scp client/build/bundle/main.bundle.js root@46.254.19.56:/var/www/duma/kraski/main.bundle.js
	ssh root@46.254.19.56