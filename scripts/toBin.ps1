$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$BinDir = Join-Path $ScriptDir "..\bin"

native-image -cp .\target\github-scanner-0.0.1-SNAPSHOT.jar `
    -H:Path="$BinDir" `
    -H:Name=github-scanner `
    -H:ReflectionConfigurationFiles=./src/main/resources/META-INF/native-image/reflect-config.json `
    -H:ResourceConfigurationFiles=./src/main/resources/META-INF/native-image/resource-config.json `
    -H:DynamicProxyConfigurationFiles=./src/main/resources/META-INF/native-image/proxy-config.json `
    -H:Class="org.grano22.dev.githubscanner.cli.GithubScannerCLIApplication" `
    --verbose --diagnostics-mode -H:-CheckToolchain --no-fallback org.grano22.dev.githubscanner.cli.GithubScannerCLIApplication

