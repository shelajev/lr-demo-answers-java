#!/bin/bash

outputdir="$1"

if [ "x$outputdir" == "x" ]; then
  outputdir=".."
fi

function package {
  git checkout $1
  git pull
  mvn clean package
  mv webapp/target/lr-demo-answers-java-$2.war "$3/"
}

mkdir -p "$outputdir"
package v1 1.0 "$outputdir"
package v2 2.0 "$outputdir"
package v3 3.0 "$outputdir"
package master 4.0 "$outputdir"
