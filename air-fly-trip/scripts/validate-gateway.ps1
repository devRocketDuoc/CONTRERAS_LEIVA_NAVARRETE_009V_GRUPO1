$ErrorActionPreference = "Stop"

$scriptDirectory = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location (Join-Path $scriptDirectory "..")

$eurekaUrl = "http://localhost:8761/"
$gatewayUrl = "http://localhost:8080"
$email = "gateway.$([DateTimeOffset]::UtcNow.ToUnixTimeMilliseconds())@airflytrip.cl"
$password = "ClaveSegura123"

function Invoke-Http {
    param(
        [string]$Method,
        [string]$Url,
        [string]$Body = "",
        [hashtable]$Headers = @{}
    )

    $requestParameters = @{
        Uri = $Url
        Method = $Method
        UseBasicParsing = $true
    }

    if ($Headers.Count -gt 0) {
        $requestParameters.Headers = $Headers
    }

    if ($Body -ne "") {
        $requestParameters.Body = $Body
        if (-not $Headers.ContainsKey("Content-Type")) {
            $requestParameters.ContentType = "application/json"
        }
    }

    try {
        $response = Invoke-WebRequest @requestParameters
        return [pscustomobject]@{
            StatusCode = [int]$response.StatusCode
            Body = $response.Content
        }
    } catch {
        if (-not $_.Exception.Response) {
            throw
        }

        $response = $_.Exception.Response
        $bodyContent = ""

        if ($response.GetResponseStream()) {
            $reader = New-Object System.IO.StreamReader($response.GetResponseStream())
            $bodyContent = $reader.ReadToEnd()
            $reader.Dispose()
        }

        return [pscustomobject]@{
            StatusCode = [int]$response.StatusCode
            Body = $bodyContent
        }
    }
}

function Assert-Status {
    param(
        [string]$Name,
        [pscustomobject]$Response,
        [int]$Expected
    )

    if ($Response.StatusCode -ne $Expected) {
        Write-Host "[air-fly-trip] FAIL $Name -> esperado $Expected, recibido $($Response.StatusCode)"
        if ($Response.Body) {
            Write-Host $Response.Body
        }
        exit 1
    }

    Write-Host "[air-fly-trip] OK $Name -> $Expected"
}

function Assert-StatusIn {
    param(
        [string]$Name,
        [pscustomobject]$Response,
        [int[]]$Expected
    )

    if ($Expected -notcontains $Response.StatusCode) {
        $expectedText = ($Expected | ForEach-Object { $_.ToString() }) -join ", "
        Write-Host "[air-fly-trip] FAIL $Name -> esperado uno de [$expectedText], recibido $($Response.StatusCode)"
        if ($Response.Body) {
            Write-Host $Response.Body
        }
        exit 1
    }

    Write-Host "[air-fly-trip] OK $Name -> $($Response.StatusCode)"
}

function Wait-For-Status {
    param(
        [string]$Name,
        [string]$Method,
        [string]$Url,
        [int]$Expected,
        [int]$Attempts = 18,
        [int]$DelaySeconds = 5
    )

    for ($attempt = 1; $attempt -le $Attempts; $attempt++) {
        $response = Invoke-Http -Method $Method -Url $Url

        if ($response.StatusCode -eq $Expected) {
            Write-Host "[air-fly-trip] OK $Name -> $Expected"
            return
        }

        if ($attempt -lt $Attempts) {
            Start-Sleep -Seconds $DelaySeconds
        }
    }

    Write-Host "[air-fly-trip] FAIL $Name -> esperado $Expected"
    exit 1
}

$registerBody = @{
    email = $email
    password = $password
    firstName = "Tester"
    lastName = "Gateway"
    documentNumber = "12345678-9"
    phone = "+56912345678"
    role = "CLIENT"
} | ConvertTo-Json -Compress

$loginBody = @{
    email = $email
    password = $password
} | ConvertTo-Json -Compress

$jsonHeaders = @{}

$eurekaResponse = Invoke-Http -Method "GET" -Url $eurekaUrl
Assert-Status -Name "eureka" -Response $eurekaResponse -Expected 200

$gatewayRootResponse = Invoke-Http -Method "GET" -Url "$gatewayUrl/"
Assert-Status -Name "gateway-root" -Response $gatewayRootResponse -Expected 404

Wait-For-Status -Name "auth-route-ready" -Method "GET" -Url "$gatewayUrl/api/v1/auth/me" -Expected 401

$registerResponse = Invoke-Http -Method "POST" -Url "$gatewayUrl/api/v1/auth/register" -Body $registerBody -Headers $jsonHeaders
Assert-Status -Name "auth-register" -Response $registerResponse -Expected 201

$loginResponse = Invoke-Http -Method "POST" -Url "$gatewayUrl/api/v1/auth/login" -Body $loginBody -Headers $jsonHeaders
Assert-Status -Name "auth-login" -Response $loginResponse -Expected 200

$loginPayload = $loginResponse.Body | ConvertFrom-Json
$token = $loginPayload.token
$userId = $loginPayload.user.id

if ([string]::IsNullOrWhiteSpace($token)) {
    Write-Host "[air-fly-trip] FAIL auth-token -> no se recibio token"
    exit 1
}

if (-not $userId) {
    Write-Host "[air-fly-trip] FAIL auth-user-id -> no se recibio el identificador del usuario autenticado"
    exit 1
}

$authHeaders = @{
    "Authorization" = "Bearer $token"
}

$authMeUnauthorizedResponse = Invoke-Http -Method "GET" -Url "$gatewayUrl/api/v1/auth/me"
Assert-Status -Name "auth-me-without-token" -Response $authMeUnauthorizedResponse -Expected 401

$authMeAuthorizedResponse = Invoke-Http -Method "GET" -Url "$gatewayUrl/api/v1/auth/me" -Headers $authHeaders
Assert-Status -Name "auth-me-with-token" -Response $authMeAuthorizedResponse -Expected 200

$protectedEndpoints = @(
    "/api/v1/routes",
    "/api/v1/vehicles",
    "/api/v1/trips/user/$userId",
    "/api/v1/reservations/user/$userId",
    "/api/v1/tariffs/active",
    "/api/v1/payments/trip/1",
    "/api/v1/notifications/user/$userId",
    "/api/v1/terminals",
    "/api/v1/charging-stations"
)

foreach ($path in $protectedEndpoints) {
    $name = $path.TrimStart("/")

    $unauthorizedResponse = Invoke-Http -Method "GET" -Url "$gatewayUrl$path"
    Assert-StatusIn -Name "$name-without-token" -Response $unauthorizedResponse -Expected @(401, 403)

    $authorizedResponse = Invoke-Http -Method "GET" -Url "$gatewayUrl$path" -Headers $authHeaders
    Assert-Status -Name "$name-with-token" -Response $authorizedResponse -Expected 200
}

Write-Host "[air-fly-trip] Validacion del gateway finalizada correctamente."
