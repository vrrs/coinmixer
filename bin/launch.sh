#!/bin/bash
ENV="dev"

if [ $# -gt 0 ]; then
	ENV=$1
fi
	
java -jar ~/coinmixer/lib/coinmixer*.jar server ~/coinmixer/conf/${ENV}/coinmixer.yml &