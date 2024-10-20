FROM alpine:3.17

RUN apk add --no-cache &&\
    wget -O /etc/apk/keys/amazoncorretto.rsa.pub https://apk.corretto.aws/amazoncorretto.rsa.pub && \
    echo "https://apk.corretto.aws" >> /etc/apk/repositories && \
    apk update &&\
    apk add amazon-corretto-17 curl

COPY /archetype-app/build/libs/archetype-app*.jar app.jar
COPY certs certs
RUN for f in /certs/*.cer; do keytool -import -alias "$f" -file "$f" -noprompt -cacerts -storepass changeit; done

EXPOSE 8080

RUN sh -c 'touch /app.jar'

ENTRYPOINT ["java","-jar","/app.jar"]
