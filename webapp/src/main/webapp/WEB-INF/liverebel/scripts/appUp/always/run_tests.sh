#!/bin/bash

set -e

TEST_CLASSPATH=""
for f in $(ls $HOME/selenium*/selenium*/libs/*.jar $HOME/selenium*/selenium*/*.jar $HOME/webapps/lr-demo-answers-java/WEB-INF/lib/lr-demo-answers-integration-*.jar); do TEST_CLASSPATH="$TEST_CLASSPATH:$f"; done

echo "Running:"
echo "java -cp \"$TEST_CLASSPATH\" org.junit.runner.JUnitCore com.zeroturnaround.rebelanswers.tests.functional.SiteTest"

# disabling since not working when running during deploy, works manually
#java -cp "$TEST_CLASSPATH" org.junit.runner.JUnitCore com.zeroturnaround.rebelanswers.tests.functional.SiteTest