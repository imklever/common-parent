#中石油用到的jar
#mvn install:install-file -Dfile=libs/aliyun-java-sdk-dysmsapi-1.0.0-SANPSHOT.jar -DgroupId=aliyun.java.sdk.dysmsapi   -DartifactId=dysmsapi -Dversion=1.0.0 -Dpackaging=jar
#mvn install:install-file -Dfile=libs/alicom-mns-receive-sdk-0.0.1-SNAPSHOT.jar -DgroupId=com.aliyun.alicom   -DartifactId=alicom-mns-receive-sdk -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar
#mvn install:install-file -Dfile=libs/aliyun-sdk-mns-1.1.8.jar -DgroupId=com.aliyun.mns   -DartifactId=aliyun-sdk-mns -Dversion=1.1.8 -Dpackaging=jar
#mvn install:install-file -Dfile=libs/aliyun-java-sdk-core-3.2.3.jar -DgroupId=com.aliyun   -DartifactId=aliyun-java-sdk-core -Dversion=3.2.3 -Dpackaging=jar


mvn install:install-file -Dfile=lib/x-pack-sql-jdbc-6.6.0.jar    -DgroupId=org.elasticsearch.xpack -DartifactId=sqljdbc -Dversion=6.6.0 -Dpackaging=jar

mvn install:install-file -Dfile=x-pack-sql-jdbc-6.6.0.jar    -DgroupId=org.elasticsearch.xpack -DartifactId=sqljdbc -Dversion=6.6.0 -Dpackaging=jar


mvn install:install-file -Dfile=lib/antlr-2.7.2.jar             -DgroupId=antlr -DartifactId=antlr -Dversion=2.7.2 -Dpackaging=jar
mvn install:install-file -Dfile=lib/avalon-framework-4.1.5.jar  -DgroupId=org.apache.avalon -DartifactId=framework -Dversion=4.1.5 -Dpackaging=jar
mvn install:install-file -Dfile=lib/idl.jar                     -DgroupId=org.jacorb       -DartifactId=idl -Dversion=2.2.4 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jacorb.jar                  -DgroupId=org.jacorb       -DartifactId=jacorb -Dversion=2.2.4 -Dpackaging=jar
mvn install:install-file -Dfile=lib/u2000_idl.jar               -DgroupId=com.huawei       -DartifactId=u2000  -Dversion=2.2.4 -Dpackaging=jar
mvn install:install-file -Dfile=lib/wrapper-3.1.0.jar           -DgroupId=org.tanukisoftware -DartifactId=wrapper  -Dversion=2.2.4 -Dpackaging=jar