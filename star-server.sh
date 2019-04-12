svn up
mvn clean package -Dmaven.test.skip
unalias cp
cp -rf api/target/api-0.0.1-SNAPSHOT.jar runnable-jars/service/api.jar
cp -rf service-backup/target/service-backup-0.0.1-SNAPSHOT.jar runnable-jars/service/service-backup.jar
cp -rf service-mongo/target/service-mongo-0.0.1-SNAPSHOT.jar   runnable-jars/service/service-mongo.jar




nohup java -jar runnable-jars/service/api.jar             --server.port=8111 >runnable-jars/logs/api-8111.log  2>&1 &
nohup java -jar runnable-jars/service/service-backup.jar  --server.port=8121 --spring.profiles.active=jd    >runnable-jars/logs/service-backup-8121.log  2>&1 &
nohup java -jar runnable-jars/service/service-mongo.jar   --server.port=8151 --spring.profiles.active=jd    >runnable-jars/logs/service-mongo-8151.log   2>&1 &
