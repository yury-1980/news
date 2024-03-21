FROM openjdk:17-alpine
WORKDIR /app
COPY ./build/libs/*.jar /app/
RUN apk update && apk add --no-cache bash
EXPOSE 8080
ENTRYPOINT ["java", "-jar"]
CMD ["news-comment-0.0.1-SNAPSHOT.jar"]
