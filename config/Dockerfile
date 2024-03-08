#FROM openjdk:17-alpine
#EXPOSE 8888
#ADD ./build/libs/config-0.0.1-SNAPSHOT.jar /app/app.jar
#ENTRYPOINT ["java","-jar","/app/app.jar"]

#FROM openjdk:17-alpine
##EXPOSE 8888
#ARG JAR_FILE=build/libs/config-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} config-0.0.1-SNAPSHOT.jar
#ENTRYPOINT ["java","-jar","/config-0.0.1-SNAPSHOT.jar"]


FROM openjdk:17-alpine
WORKDIR /app
COPY ./build/libs/*.jar /app/
RUN apk update && apk add --no-cache bash
ENTRYPOINT ["java", "-jar"]
CMD ["config-0.0.1-SNAPSHOT.jar"]