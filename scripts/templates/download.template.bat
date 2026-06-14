@echo off

echo "Fetching Database from remote"

cd Satyam-Database-{{PLACEHOLDER_YEAR}}
git pull origin main

git reset --hard
copy database.db ..\database.db

pause
