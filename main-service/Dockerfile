FROM amazoncorretto:11

WORKDIR main_server_app

COPY ./target/*.jar app.jar

ENTRYPOINT ["java","-jar","/main_server_app/app.jar"]