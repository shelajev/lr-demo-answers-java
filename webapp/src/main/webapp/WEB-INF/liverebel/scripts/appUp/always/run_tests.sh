#!/bin/bash

set -e

remoteport=$(cat "$HOME/tunnelport")
port=$( echo "$directUrls" | /opt/vagrant_ruby/bin/ruby -ruri -e 'puts URI.parse(gets.chomp).port' )
ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -nNT -R $remoteport:localhost:$port vagrant@java.answers.liverebel.com &
PID=$!

TEST_CLASSPATH=""
for f in $(ls $HOME/selenium*/selenium*/libs/*.jar $HOME/selenium*/selenium*/*.jar $HOME/webapps/lr-demo-answers-java/WEB-INF/lib/lr-demo-answers-integration-*.jar); do TEST_CLASSPATH="$TEST_CLASSPATH:$f"; done
java -cp "$TEST_CLASSPATH" -DremotePort=$remoteport org.junit.runner.JUnitCore com.zeroturnaround.rebelanswers.tests.functional.SiteTest

kill $PID