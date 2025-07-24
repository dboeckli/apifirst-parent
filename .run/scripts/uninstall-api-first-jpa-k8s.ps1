cd api-first-server-jpa/target/helm/repo

$file = Get-ChildItem -Filter api-first-server-jpa-v*.tgz | Select-Object -First 1
$APPLICATION_NAME = Get-ChildItem -Directory | Where-Object { $_.LastWriteTime -ge $file.LastWriteTime } | Select-Object -ExpandProperty Name

helm uninstall $APPLICATION_NAME --namespace api-first-server-jpa
