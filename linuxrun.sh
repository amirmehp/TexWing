#!/bin/bash

javac -d . src/Main/*.java

java Main/Main

rm -rf Main
