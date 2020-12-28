#!/bin/sh

#initialize the Certificate Authority directory

mkdir testca
cd testca
mkdir certs private
chmod 700 private
echo 01 > serial
touch index.txt
