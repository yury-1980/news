FROM openjdk:17-alpine
WORKDIR /app
COPY ./build/libs/*.jar /app/
RUN apk --update --no-cache add curl
EXPOSE 8888
ENTRYPOINT ["java", "-jar"]
CMD ["config-0.0.1-SNAPSHOT.jar"]