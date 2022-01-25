dist:
		./gradlew build

image:
		./gradlew jibDockerBuild --image=markets-api

run:	image
		docker-compose up -d

stop:
		docker-compose down