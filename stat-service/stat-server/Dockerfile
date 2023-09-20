FROM amazoncorretto:11

WORKDIR stat_server_app

COPY ./target/*.jar app.jar

ENTRYPOINT ["java","-jar","/stat_server_app/app.jar"]