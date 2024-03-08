#FROM openjdk:17-alpine
#EXPOSE 8080
#ADD ./build/libs/news-comment-0.0.1-SNAPSHOT.jar /app/app.jar
#ENTRYPOINT ["java","-Dspring.profiles.active=prod,test","-jar","/app/app.jar"]


FROM openjdk:17-alpine
LABEL maintainer="Yuriy"
WORKDIR /app
COPY ./build/libs/*.jar /app/
RUN apk update && apk add --no-cache bash
ENTRYPOINT ["java", "-jar"]
CMD ["news-comment-0.0.1-SNAPSHOT.jar"]
#
#
#FROM openjdk:17-alpine
##EXPOSE 8080
#ARG JAR_FILE=build/libs/news-comment-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} news-comment-0.0.1-SNAPSHOT.jar
#ENTRYPOINT ["java","-jar","/news-comment-0.0.1-SNAPSHOT.jar"]
