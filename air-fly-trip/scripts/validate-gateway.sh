#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

EUREKA_URL="http://localhost:8761/"
GATEWAY_URL="http://localhost:8080"
EMAIL="gateway.$(date +%s).$$@airflytrip.cl"
PASSWORD="ClaveSegura123"
HTTP_STATUS=""
HTTP_BODY=""

request() {
  local method="$1"
  local url="$2"
  local body="${3-}"
  local auth_token="${4-}"
  local response_file
  response_file="$(mktemp)"
  local args=(-s -o "$response_file" -w "%{http_code}" -X "$method")

  if [[ -n "$auth_token" ]]; then
    args+=(-H "Authorization: Bearer $auth_token")
  fi

  if [[ -n "$body" ]]; then
    args+=(-H "Content-Type: application/json" --data-raw "$body")
  fi

  args+=("$url")

  HTTP_STATUS="$(curl "${args[@]}")"
  HTTP_BODY="$(cat "$response_file")"
  rm -f "$response_file"
}

assert_status() {
  local name="$1"
  local expected="$2"

  if [[ "$HTTP_STATUS" != "$expected" ]]; then
    echo "[air-fly-trip] FAIL $name -> esperado $expected, recibido $HTTP_STATUS"
    if [[ -n "$HTTP_BODY" ]]; then
      echo "$HTTP_BODY"
    fi
    exit 1
  fi

  echo "[air-fly-trip] OK $name -> $expected"
}

assert_status_in() {
  local name="$1"
  shift

  for expected in "$@"; do
    if [[ "$HTTP_STATUS" == "$expected" ]]; then
      echo "[air-fly-trip] OK $name -> $HTTP_STATUS"
      return
    fi
  done

  echo "[air-fly-trip] FAIL $name -> recibido $HTTP_STATUS"
  if [[ -n "$HTTP_BODY" ]]; then
    echo "$HTTP_BODY"
  fi
  exit 1
}

wait_for_status() {
  local name="$1"
  local method="$2"
  local url="$3"
  local expected="$4"
  local body="${5-}"
  local auth_token="${6-}"

  for _ in $(seq 1 18); do
    request "$method" "$url" "$body" "$auth_token"

    if [[ "$HTTP_STATUS" == "$expected" ]]; then
      echo "[air-fly-trip] OK $name -> $expected"
      return
    fi

    sleep 5
  done

  echo "[air-fly-trip] FAIL $name -> esperado $expected, recibido $HTTP_STATUS"
  if [[ -n "$HTTP_BODY" ]]; then
    echo "$HTTP_BODY"
  fi
  exit 1
}

REGISTER_BODY=$(cat <<JSON
{"email":"$EMAIL","password":"$PASSWORD","firstName":"Tester","lastName":"Gateway","documentNumber":"12345678-9","phone":"+56912345678","role":"CLIENT"}
JSON
)

LOGIN_BODY=$(cat <<JSON
{"email":"$EMAIL","password":"$PASSWORD"}
JSON
)

request "GET" "$EUREKA_URL"
assert_status "eureka" "200"

request "GET" "$GATEWAY_URL/"
assert_status "gateway-root" "404"

wait_for_status "auth-route-ready" "GET" "$GATEWAY_URL/api/v1/auth/me" "401"

request "POST" "$GATEWAY_URL/api/v1/auth/register" "$REGISTER_BODY"
assert_status "auth-register" "201"

request "POST" "$GATEWAY_URL/api/v1/auth/login" "$LOGIN_BODY"
assert_status "auth-login" "200"

TOKEN="$(printf '%s' "$HTTP_BODY" | tr -d '\r\n' | sed -n 's/.*"token"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/p')"
USER_ID="$(printf '%s' "$HTTP_BODY" | tr -d '\r\n' | sed -n 's/.*"user"[[:space:]]*:[[:space:]]*{[^}]*"id"[[:space:]]*:[[:space:]]*\([0-9][0-9]*\).*/\1/p')"

if [[ -z "$TOKEN" ]]; then
  echo "[air-fly-trip] FAIL auth-token -> no se recibio token"
  exit 1
fi

if [[ -z "$USER_ID" ]]; then
  echo "[air-fly-trip] FAIL auth-user-id -> no se recibio el identificador del usuario autenticado"
  exit 1
fi

request "GET" "$GATEWAY_URL/api/v1/auth/me"
assert_status "auth-me-without-token" "401"

request "GET" "$GATEWAY_URL/api/v1/auth/me" "" "$TOKEN"
assert_status "auth-me-with-token" "200"

for path in \
  "/api/v1/routes" \
  "/api/v1/vehicles" \
  "/api/v1/trips/user/$USER_ID" \
  "/api/v1/reservations/user/$USER_ID" \
  "/api/v1/tariffs/active" \
  "/api/v1/payments/trip/1" \
  "/api/v1/notifications/user/$USER_ID" \
  "/api/v1/terminals" \
  "/api/v1/charging-stations"
do
  name="${path#/}"

  request "GET" "$GATEWAY_URL$path"
  assert_status_in "${name}-without-token" "401" "403"

  request "GET" "$GATEWAY_URL$path" "" "$TOKEN"
  assert_status "${name}-with-token" "200"
done

echo "[air-fly-trip] Validacion del gateway finalizada correctamente."
