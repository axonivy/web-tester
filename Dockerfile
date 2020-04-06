FROM selenium/standalone-firefox:3.141.59

RUN \
    sudo apt-get -y update && \
    sudo apt-get install -y maven && \
    sudo apt-get install -y openjdk-11-jdk-headless && \
    sudo apt-get install -y gnupg2 && \
    sudo apt-get install -y git
