@echo off

echo "Fetching Database from remote"

cd Satyam-Database-2026-27
git pull origin main

git reset --hard
copy database.db ..\database.db

pause
