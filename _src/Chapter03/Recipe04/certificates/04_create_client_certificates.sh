#!/bin/sh

mkdir -p client
cd client

# create the client private key
openssl genrsa -out key.pem 2048

# create a certificate signing request
openssl req -new -key key.pem -out req.pem -outform PEM \
    -subj /CN=$(hostname)/O=client/ -nodes

# sign the certificate request in the CA
cd ../testca
openssl ca -config openssl.cnf -in ../client/req.pem -out \
    ../client/cert.pem -notext -batch -extensions client_ca_extensions

# create a PKCS#12 store, containing the client certificate and key protected by a password
cd ../client
openssl pkcs12 -export -out keycert.p12 -in cert.pem -inkey key.pem -passout pass:client1234passwd
