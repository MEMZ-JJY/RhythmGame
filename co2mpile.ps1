# Clean up display
Clear-Host
Write-Host "--- RhythmGame Compilation ---" -ForegroundColor Cyan

# 1. Check Library
$libFile = "lib/json-20231013.jar"
if (-not (Test-Path $libFile)) {
    Write-Host "[ERROR] Cannot find: $libFile" -ForegroundColor Red
    Pause
    exit
}

# 2. Create bin folder
if (-not (Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
    Write-Host "[INFO] Created 'bin' folder."
}

# 3. Get Source Files
# This finds all .java files in the package directory
$sourceFiles = Get-ChildItem "src/com/rhythmgame/*.java" | ForEach-Object { $_.FullName }

Write-Host "[INFO] Compiling..." -ForegroundColor Yellow

# 4. Run Javac
# Using -cp "lib/*" directly is safest for Java
javac -encoding UTF-8 -d "bin" -cp "lib/*" $sourceFiles

# 5. Check Result
if ($LASTEXITCODE -eq 0) {
    Write-Host "----------------------------"
    Write-Host "SUCCESS: Compilation finished!" -ForegroundColor Green
    Write-Host "Run the game using: .\run.ps1"
    Write-Host "----------------------------"
} else {
    Write-Host "[FAILED] Compilation errors occurred." -ForegroundColor Red
}

Pause