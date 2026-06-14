import platform
import re
import subprocess
from pathlib import Path

year = str(input("Enter financial year in the format like (2026-27): "))


def check_valid_financial_year(year: str):
    if not re.fullmatch(r"\d{4}-\d{2}", year):
        return False
    start_year = year.split("-")[0][2:]
    end_year = year.split("-")[1]
    return int(start_year) + 1 == int(end_year)


def replace_in_template(input_file, output_file, placeholder, replacement):
    with open(f"templates/{input_file}", "r") as f:
        lines = f.readlines()
    new_lines = []
    for line in lines:
        new_line = line.replace(placeholder, replacement)
        new_lines.append(new_line)
    with open(output_file, "w") as f:
        f.writelines(new_lines)


def generate_bash_scripts():
    replace_in_template(
        "download.template.sh", "download.sh", "{{PLACEHOLDER_YEAR}}", year
    )
    replace_in_template("upload.template.sh", "upload.sh", "{{PLACEHOLDER_YEAR}}", year)


def generate_batch_scripts():
    replace_in_template(
        "download.template.bat", "download.bat", "{{PLACEHOLDER_YEAR}}", year
    )
    replace_in_template(
        "upload.template.bat", "upload.bat", "{{PLACEHOLDER_YEAR}}", year
    )


def init_github_repository():
    repo_name = f"Satyam-Database-{year}"
    if (Path(repo_name) / ".git").is_dir():
        print(f"{repo_name} already exists, so skipping clone")
        return
    github_username = "drcbabu81"
    repo_url = f"git@github.com:{github_username}/{repo_name}.git"

    try:
        subprocess.run(
            ["git", "clone", repo_url], check=True, capture_output=True, text=True
        )

        if (Path(repo_name) / ".git").is_dir():
            print("Clone successful")
        else:
            print("Git returned success but .git directory is missing")

    except subprocess.CalledProcessError as e:
        print("Clone failed")
        print(e.stderr)


if not check_valid_financial_year(year):
    print("Please enter a valid financial year in the format like (2026-27)")
    exit(0)

os_name = platform.system()
init_github_repository()

if os_name == "Windows":
    print("Running on Windows")
    generate_batch_scripts()
elif os_name == "Darwin":
    print("Running on macOS")
    generate_bash_scripts()
elif os_name == "Linux":
    print("Running on Linux")
    generate_bash_scripts()
else:
    print(f"Unknown OS: {os_name}")
    exit(1)
