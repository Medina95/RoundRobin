FROM openjdk:17
ENV PORT=5000
EXPOSE 5000
ENTRYPOINT ["java","-jar","springboot-docker-compose-round.jar"]
WORKDIR /usrapp/bin
COPY /target/classes /usrapp/bin/classes
COPY /target/dependency /usrapp/bin/dependency
ADD target/springboot-docker-compose-round.jar /usrapp/bin/springboot-docker-compose-round.jar

CMD ["java","-cp","./classes:./dependency/*","org.example.RestServiceApplication"]