#!/bin/bash


### DOESN'T WORK! DON'T USE! ###


declare -a arr=("NKss""KNqKEEsrCrsEAKqssCsq""NENrr""NAsr")

javac woofFinder.java
java woofFinder

for i in "${arr[@]}"
do
    echo "$i"
done

