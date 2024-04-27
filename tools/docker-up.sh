#!/usr/bin/env bash

cd $(dirname $0) || exit
cd ../docker || exit

echo "+++++++++++++++++++++++++++++++++++++++"
echo "Start to docker compose up..."
echo "+++++++++++++++++++++++++++++++++++++++"

TMPDIR=/private$TMPDIR docker compose up -d --build
