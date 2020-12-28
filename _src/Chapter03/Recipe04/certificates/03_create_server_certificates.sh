#!/bin/sh


mkdir server
cd server
# create the server private key
openssl genrsa -out key.pem 2048

# create a certificate signing request
openssl req -new -key key.pem -out req.pem -outform PEM \
    -subj /CN=$(hostname)/O=server/ -nodes

# sign the certificate request in the CA
cd ../testca
openssl ca -config openssl.cnf -in ../server/req.pem -out \
    ../server/cert.pem -notext -batch -extensions server_ca_extensions
