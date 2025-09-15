# Simple Load Test Script
# Install: choco install curl (or use Invoke-WebRequest)

$baseUrl = "http://localhost:8080"
$iterations = 100
$concurrency = 10

Write-Host "Starting Load Test..." -ForegroundColor Green
Write-Host "Base URL: $baseUrl" -ForegroundColor Yellow
Write-Host "Iterations: $iterations, Concurrency: $concurrency" -ForegroundColor Yellow

# Test 1: Home endpoint
Write-Host "`nTesting Home Endpoint..." -ForegroundColor Cyan
$jobs = @()
for ($i = 1; $i -le $concurrency; $i++) {
    $jobs += Start-Job -ScriptBlock {
        param($url, $iter)
        $results = @()
        for ($j = 1; $j -le $iter; $j++) {
            $start = Get-Date
            try {
                $response = Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 30
                $end = Get-Date
                $duration = ($end - $start).TotalMilliseconds
                $results += @{
                    Success = $true
                    StatusCode = $response.StatusCode
                    Duration = $duration
                }
            } catch {
                $end = Get-Date
                $duration = ($end - $start).TotalMilliseconds
                $results += @{
                    Success = $false
                    Error = $_.Exception.Message
                    Duration = $duration
                }
            }
        }
        return $results
    } -ArgumentList "$baseUrl/", ($iterations / $concurrency)
}

$allResults = $jobs | Wait-Job | Receive-Job
$jobs | Remove-Job

# Analyze results
$successful = ($allResults | Where-Object { $_.Success -eq $true }).Count
$failed = ($allResults | Where-Object { $_.Success -eq $false }).Count
$avgDuration = ($allResults | Measure-Object -Property Duration -Average).Average

Write-Host "`nResults:" -ForegroundColor Green
Write-Host "Total Requests: $($successful + $failed)" -ForegroundColor White
Write-Host "Successful: $successful" -ForegroundColor Green
Write-Host "Failed: $failed" -ForegroundColor Red
Write-Host "Average Response Time: $([math]::Round($avgDuration, 2)) ms" -ForegroundColor Yellow

if ($failed -gt 0) {
    Write-Host "`nErrors found! Check application logs." -ForegroundColor Red
} else {
    Write-Host "`nAll requests successful!" -ForegroundColor Green
}
