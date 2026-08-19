cd apifirst-server/target/helm/repo

$file = Get-ChildItem -Filter apifirst-server-chart-*.tgz | Select-Object -First 1
$APPLICATION_NAME = Get-ChildItem -Directory | Where-Object { $_.LastWriteTime -ge $file.LastWriteTime } | Select-Object -ExpandProperty Name

helm uninstall $APPLICATION_NAME --namespace apifirst-server

kubectl delete pod -n apifirst-server --field-selector=status.phase==Succeeded
kubectl delete pod -n apifirst-server --field-selector=status.phase==Failed
