#!/bin/bash
cd ~/dev/spring-sample-truck-rest-backend-mysql
cd spring-sample-truck-frontend
./gradlew clean bootJar

cd ~/dev/spring-sample-truck-rest-backend-mysql
cd spring-sample-truck-rest
./gradlew clean bootJar

cd ~/dev/spring-sample-truck-rest-backend-mysql
cd spring-sample-truck-backend
./gradlew clean bootJar