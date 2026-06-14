#!/usr/bin/env bash

DIR=Satyam-Database-{{PLACEHOLDER_YEAR}}

if [ ! -d "$DIR" ]; then
    echo "$DIR Directory does not exist"
    exit 1
fi

cp database.db $DIR\database.db

cd $DIR

git add .
git commit -m "Updated database"
git push origin main
