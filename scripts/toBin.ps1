#mvn spring-aot:generate
#C:\Users\adrianpc21\.java\graalvm-jdk-21\bin\native-image.cmd -jar .\target\github-scanner-0.0.1-SNAPSHOT.jar -H:Path=C:\Users\adrianpc21\IdeaProjects\github-scanner\bin -H:Name=github-scanner --verbose --diagnostics-mode -H:-CheckToolchain --no-fallback

#mvn -Pnative test native:metadata-copy
#java -agentlib:native-image-agent=config-output-dir=./src/main/resources/META-INF/native-image -jar .\target\github-scanner-0.0.1-SNAPSHOT.jar

native-image -cp .\target\github-scanner-0.0.1-SNAPSHOT.jar `
    -H:Path=C:\Users\adrianpc21\IdeaProjects\github-scanner\bin `
    -H:Name=github-scanner `
    -H:ReflectionConfigurationFiles=./src/main/resources/META-INF/native-image/reflect-config.json `
    -H:ResourceConfigurationFiles=./src/main/resources/META-INF/native-image/resource-config.json `
    -H:DynamicProxyConfigurationFiles=./src/main/resources/META-INF/native-image/proxy-config.json `
    -H:Class="org.grano22.dev.githubscanner.cli.GithubScannerCLIApplication" `
    --verbose --diagnostics-mode -H:-CheckToolchain --no-fallback org.grano22.dev.githubscanner.cli.GithubScannerCLIApplication

