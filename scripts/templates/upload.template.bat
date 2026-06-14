@echo off
copy database.db Satyam-Database-{{PLACEHOLDER_YEAR}}\database.db
cd Satyam-Database-{{PLACEHOLDER_YEAR}}
git add .
git commit -m "Updated database"
git push origin main
pause
