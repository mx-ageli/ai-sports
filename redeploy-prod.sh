#进入代码文件夹，必须有git管理
cd /home/AiSports/ai-sports
#更新代码
git pull
#清理原来的jar包重新打包
mvn clean install -Dmaven.test.skip=true
#cd ~
#删除原来的jar包
#rm -rf ai-sports-1.0-SNAPSHOT.jar.jar
#cp code/test/test-web/target/test-web.jar test-web.jar
#后台运行
#nohup java -agentlib:jdwp=transport=dt_socket,address=8100,server=y,suspend=n -jar test-web.jar > /root/logs/test.log &

#杀死原来的java进程
jarName="ai-sports-1.0-SNAPSHOT.jar"

count=$(ps -ef |grep ${jarName} |grep -v "grep" |wc -l)

if [ ${count} -gt 0 ]; then
        echo "已存在${count}个${jarName}程序在运行"
        jarPid=$(ps x |grep ${jarName} | grep -v grep | awk '{print $1}')
        echo ${jarPid}
        kill -9 ${jarPid}
        output=`echo "正在关闭${jarName}程序,进程id${jarPid}"`
        echo ${output}
else
        echo "没有对应的程序在运行"
fi

nohup java -jar /home/AiSports/ai-sports/target/ai-sports-1.0-SNAPSHOT.jar --spring.profiles.active=prod&
#监控日志
tail -f /home/AiSports/ai-sports/nohup.out