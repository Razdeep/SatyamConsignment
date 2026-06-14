#!/usr/bin/env bash

DIR=Satyam-Database-{{PLACEHOLDER_YEAR}}

if [ ! -d "$DIR" ]; then
    echo "$DIR Directory does not exist"
    exit 1
fi

cd $DIR || exit 1

echo "Fetching Database from remote"
git pull origin main

git reset --hard
cp database.db ../database.db
