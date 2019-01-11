FROM confluentinc/cp-kafka-connect:3.2.0

WORKDIR /kafka-connect-source-github
COPY config config
COPY build build

VOLUME /kafka-connect-source-github/config
VOLUME /kafka-connect-source-github/offsets

CMD CLASSPATH="$(find build/ -type f -name '*.jar' | tr '\n' ':')" connect-standalone config/worker.properties config/connector.properties
