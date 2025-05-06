#!/bin/bash

testName=${1:""}
threads=${2:-1}
connections=${3:-1}

export WRK_TEST_NAME="$testName"

wrk -t $threads -c $connections -d 10s --latency --timeout 5s \
    --script delay.lua \
    http://localhost:8080