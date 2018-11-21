FROM maven:3.5.2-jdk-8

RUN apt-get -y update && apt-get -y install gnupg2

ENV HOME: /var/app