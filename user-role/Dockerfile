#FROM openjdk:17-alpine
#EXPOSE 8081
#ADD ./build/libs/user-role-0.0.1-SNAPSHOT.jar /app/app.jar
#ENTRYPOINT ["java","-Dspring.profiles.active=prod,test","-jar","/app/app.jar"]


#FROM openjdk:17-alpine
##EXPOSE 8081
#ARG JAR_FILE=build/libs/user-role-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} user-role-0.0.1-SNAPSHOT.jar
#ENTRYPOINT ["java","-jar","/user-role-0.0.1-SNAPSHOT.jar"]



FROM openjdk:17-alpine
WORKDIR /app
COPY ./build/libs/*.jar /app/
RUN apk update && apk add --no-cache bash
ENTRYPOINT ["java", "-jar"]
CMD ["user-role-0.0.1-SNAPSHOT.jar"]