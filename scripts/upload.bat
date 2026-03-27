@echo off
copy database.db Satyam-Database-2026-27\database.db
cd Satyam-Database-2026-27
git add .
git commit -m "Updated database"
git push origin main
pause
