 #上传文件的路径需要做映射
 ln  -s /data/ipagedev/upfile /tmp/upfile
#中石油用到的jar
mvn install:install-file -Dfile=service-backup/lib/antlr-2.7.2.jar             -DgroupId=antlr -DartifactId=antlr -Dversion=2.7.2 -Dpackaging=jar
mvn install:install-file -Dfile=service-backup/lib/avalon-framework-4.1.5.jar  -DgroupId=org.apache.avalon -DartifactId=framework -Dversion=4.1.5 -Dpackaging=jar
mvn install:install-file -Dfile=service-backup/lib/idl.jar                     -DgroupId=org.jacorb       -DartifactId=idl -Dversion=2.2.4 -Dpackaging=jar
mvn install:install-file -Dfile=service-backup/lib/jacorb.jar                  -DgroupId=org.jacorb       -DartifactId=jacorb -Dversion=2.2.4 -Dpackaging=jar
mvn install:install-file -Dfile=service-backup/lib/u2000_idl.jar               -DgroupId=com.huawei       -DartifactId=u2000  -Dversion=2.2.4 -Dpackaging=jar
mvn install:install-file -Dfile=service-backup/lib/wrapper-3.1.0.jar           -DgroupId=org.tanukisoftware -DartifactId=wrapper  -Dversion=2.2.4 -Dpackaging=jar
