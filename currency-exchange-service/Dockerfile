FROM openjdk

EXPOSE 8761

VOLUME /tmp
ADD target/currency-exchange-service-0.0.1-SNAPSHOT.jar currency-exchange-service-0.0.1-SNAPSHOT.jar
RUN sh -c 'touch /currency-exchange-service-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/currency-exchange-service-0.0.1-SNAPSHOT.jar"]