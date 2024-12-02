FROM eclipse-temurin:11

ARG GRPC_SERVER_PORT
ARG REDIS_HOST
ARG REDIS_PORT
ARG REDIS_USERNAME
ARG REDIS_PASSWORD
ARG REDIS_DB

#Add mantainer
LABEL org.opencontainers.image.authors="github.com/gentjankolicaj"

#Declare working dir
WORKDIR /app

#Copy project jar to docker image workdir
COPY target/garden*jar-with-dependencies.jar /app/app.jar

#Expose grpc-server port to network
EXPOSE 9001

ENTRYPOINT ["java","-jar","/app/app.jar"]