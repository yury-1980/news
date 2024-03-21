FROM openjdk:17-alpine
WORKDIR /app
COPY ./build/libs/*.jar /app/
RUN apk update && apk add --no-cache bash
EXPOSE 8081
ENTRYPOINT ["java", "-jar"]
CMD ["user-role-0.0.1-SNAPSHOT.jar"]