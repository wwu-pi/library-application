FROM jboss/wildfly:16.0.0.Final

WORKDIR $JBOSS_HOME

USER root
RUN curl -sLS https://downloads.sourceforge.net/project/hsqldb/hsqldb/hsqldb_2_4/hsqldb-2.4.1.zip > /tmp/hsqldb.zip \
 && unzip /tmp/hsqldb.zip -d /tmp \
 && mv /tmp/hsqldb-*/hsqldb/lib/hsqldb.jar /opt/hsqldb.jar \
 && rm -rf /tmp/hsqldb*

USER jboss
RUN ./bin/add-user.sh admin admin --silent \
 && (./bin/standalone.sh & ) \
 && sleep 20 \
 && ./bin/jboss-cli.sh -c 'deploy /opt/hsqldb.jar' \
 && ./bin/jboss-cli.sh -c 'data-source add --driver-name=hsqldb.jar --use-ccm=false --jta=false --user-name=sa --name=DefaultDS --jndi-name=java:/DefaultDS --connection-url=jdbc:hsqldb:${jboss.server.data.dir}${/}hypersonic${/}DefaultDS;shutdown=true' \
 && sleep 10

COPY Library/build/libs/Library-*.ear standalone/deployments/library.ear

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
