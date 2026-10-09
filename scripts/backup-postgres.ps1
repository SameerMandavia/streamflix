param(
    [string]$OutputDirectory = "./backups"
)

$ErrorActionPreference = "Stop"
New-Item -ItemType Directory -Force -Path $OutputDirectory | Out-Null
$timestamp = Get-Date -Format "yyyyMMdd-HHmmss"
$output = Join-Path $OutputDirectory "streamflix-$timestamp.dump"

$databaseUrl = if ($env:PGDATABASE) { $env:PGDATABASE } else { "streamflix" }
$databaseUser = if ($env:PGUSER) { $env:PGUSER } else { "streamflix" }
$databaseHost = if ($env:PGHOST) { $env:PGHOST } else { "localhost" }

pg_dump --format=custom --file=$output --host=$databaseHost --username=$databaseUser $databaseUrl
if ($LASTEXITCODE -ne 0) { throw "pg_dump failed with exit code $LASTEXITCODE" }
Write-Host "Backup written to $output"
