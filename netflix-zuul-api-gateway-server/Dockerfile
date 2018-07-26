FROM openjdk

EXPOSE 8761

VOLUME /tmp
ADD target/netflix-zuul-api-gateway-server-0.0.1-SNAPSHOT.jar netflix-zuul-api-gateway-server-0.0.1-SNAPSHOT.jar
RUN sh -c 'touch /netflix-zuul-api-gateway-server-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/netflix-zuul-api-gateway-server-0.0.1-SNAPSHOT.jar"]