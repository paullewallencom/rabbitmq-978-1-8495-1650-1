#!/bin/sh

mkdir -p keystore
keytool -importcert -alias server001 -file server/cert.pem -keystore keystore/rabbit.jks -keypass passwd1234
