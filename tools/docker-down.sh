#!/usr/bin/env bash

cd $(dirname $0) || exit
cd ../docker || exit

echo "+++++++++++++++++++++++++++++++++++++++"
echo "Start to docker compose down..."
echo "+++++++++++++++++++++++++++++++++++++++"

TMPDIR=/private$TMPDIR docker compose down
