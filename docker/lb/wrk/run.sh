#!/bin/bash

testName=${1:""}
threads=${2:-1}
connections=${3:-1}
host=${4:-"localhost:8080"}

export WRK_TEST_NAME="$testName"

wrk -t $threads -c $connections -d 10s --latency --timeout 5s \
    --script delay.lua \
    http://$host