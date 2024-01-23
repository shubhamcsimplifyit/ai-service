FROM maven:3.6.3-jdk-11-slim
#FROM maven:3.8.4-openjdk-17-slim
ENV HOME=/home/usr/app

RUN mkdir -p $HOME

WORKDIR $HOME

#add pom.xml only here

ADD pom.xml $HOME

#start downloading dependencies

RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "clean", "--fail-never"]

#add all source code and start compiling

ADD . $HOME

RUN ["mvn", "package"]

EXPOSE 8080

CMD ["java", "-jar", "./target/bookingservice-0.0.1-SNAPSHOT.jar"]


