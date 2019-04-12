svn up
mvn clean package -Dmaven.test.skip
unalias cp

cp platform-zuul/target/platform-zuul-0.0.1-SNAPSHOT.jar runnable-jars/platform/platform-zuul.jar
cp platform-eureka-server/target/platform-eureka-server-0.0.1-SNAPSHOT.jar runnable-jars/platform/platform-eureka.jar


nohup java -jar runnable-jars/platform/platform-zuul.jar    --server.port=8131 >runnable-jars/logs/platform-zuul-8131.log  2>&1 &
nohup java -jar runnable-jars/platform/platform-eureka.jar  --server.port=8101 >runnable-jars/logs/platform-eureka-8101.log  2>&1 &

