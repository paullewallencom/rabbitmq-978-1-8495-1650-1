#!/bin/sh

cd testca
# create Certificate Authority certificate in PEM format and a private key
openssl req -x509 -config openssl.cnf -newkey rsa:2048 -days 365 \
    -out cacert.pem -outform PEM -subj /CN=MyTestCA/ -nodes
# convert Certificate Authority certificate ti DER format
openssl x509 -in cacert.pem -out cacert.cer -outform DER
