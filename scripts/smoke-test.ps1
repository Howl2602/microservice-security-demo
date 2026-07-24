\
$ErrorActionPreference = "Stop"

Write-Host "Waiting for API Gateway..." -ForegroundColor Cyan
Start-Sleep -Seconds 10

$login = Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8085/api/auth/login" `
  -ContentType "application/json" `
  -Body '{"username":"userA","password":"123456"}'

$token = $login.token
if (-not $token) {
    throw "Login did not return a token."
}

$headers = @{ Authorization = "Bearer $token" }

Write-Host "Orders:" -ForegroundColor Cyan
Invoke-RestMethod -Uri "http://localhost:8085/api/orders" -Headers $headers |
    ConvertTo-Json -Depth 6

Write-Host "Inventory before order:" -ForegroundColor Cyan
Invoke-RestMethod -Uri "http://localhost:8085/api/inventory/Keyboard" -Headers $headers |
    ConvertTo-Json -Depth 6

Write-Host "Creating order..." -ForegroundColor Cyan
Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8085/api/orders" `
  -Headers $headers `
  -ContentType "application/json" `
  -Body '{"productName":"Keyboard","quantity":1,"totalPrice":50}' |
    ConvertTo-Json -Depth 6

Start-Sleep -Seconds 2

Write-Host "Inventory after RabbitMQ event:" -ForegroundColor Cyan
Invoke-RestMethod -Uri "http://localhost:8085/api/inventory/Keyboard" -Headers $headers |
    ConvertTo-Json -Depth 6

Write-Host "Smoke test completed." -ForegroundColor Green
