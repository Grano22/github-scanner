$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$BinDir = Join-Path $ScriptDir "..\bin"
New-Item -ItemType Directory -Force -Path $BinDir | Out-Null

$JarName = "picocli-4.7.7.jar"
$Url = "https://github.com/remkop/picocli/releases/download/v4.7.7/$JarName"
$JarPath = Join-Path $BinDir $JarName

Write-Host "Downloading Picocli CLI tool..."
Invoke-WebRequest -Uri $Url -OutFile $JarPath

Write-Host "✅ Downloaded to $JarPath"
Write-Host "👉 Run with: java -jar `"$JarPath`""