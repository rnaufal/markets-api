run:
		./gradlew jibDockerBuild --image=markets-api
		docker-compose up -d

stop:
		docker-compose down