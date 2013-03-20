#!/bin/bash

set -e

ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -nNT -R 18080:localhost:8080 vagrant@java.answers.liverebel.com &
PID=$!

TEST_CLASSPATH=""
for f in $(ls $HOME/selenium*/selenium*/libs/*.jar $HOME/selenium*/selenium*/*.jar $HOME/webapps/lr-demo-answers-java/WEB-INF/lib/lr-demo-answers-integration-*.jar); do TEST_CLASSPATH="$TEST_CLASSPATH:$f"; done
java -cp "$TEST_CLASSPATH" org.junit.runner.JUnitCore com.zeroturnaround.rebelanswers.tests.functional.SiteTest

kill $PID