#!/bin/bash

remoteport=$(cat "$HOME/tunnelport")
port=$( echo "$directUrls" | /opt/vagrant_ruby/bin/ruby -ruri -e 'puts URI.parse(gets.chomp).port' )
ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -nNT -R $remoteport:localhost:$port vagrant@java.answers.liverebel.com &
pid=$!
status=$?
if [ $status -ne 0 ]; then
  echo "Error creating SSH tunnel $remoteport:localhost:$port"
  exit $status
fi

test_classpath=""
for f in $(ls $HOME/selenium*/selenium*/libs/*.jar $HOME/selenium*/selenium*/*.jar $HOME/webapps/lr-demo-answers-java/WEB-INF/lib/lr-demo-answers-integration-*.jar); do test_classpath="$test_classpath:$f"; done
java -cp "$test_classpath" -DremotePort=$remoteport org.junit.runner.JUnitCore com.zeroturnaround.rebelanswers.tests.functional.SiteTest
status=$?

kill $pid
exit $status