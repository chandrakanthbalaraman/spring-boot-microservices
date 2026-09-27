#!/usr/bin/env bash
# Start one SB-MS process in the foreground so its logs stay in this terminal.
#
# Cursor integrated terminals (one tab per service, logs stay in the editor):
#   Ctrl+Shift+Cmd+S, or Command Palette → Tasks: Run Task → SB-MS: start all
#   ./scripts/dev-services.sh all checks ports and prints those steps.
#
# One service in the current terminal:
#   ./scripts/dev-services.sh discovery-server
#   ./scripts/dev-services.sh inventory-service 8091
#
# Stop JVMs and pause the Postgres containers (volumes are kept):
#   ./scripts/dev-services.sh stop

set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SERVICES="${ROOT}/services"
POSTGRES_DIR="${ROOT}/infrastructure/docker/postgres"

usage() {
  cat <<EOF
Usage: ./scripts/dev-services.sh <command>

  all
      Check ports, then print how to open one Cursor terminal tab per service.

  postgres
  discovery-server
  config-server
  api-gateway
  order-service
  product-service          loads repo-root .env (same file as the debug launch)
  inventory-service 8091   load-balancer instance A
  inventory-service 8092   load-balancer instance B
  stop                     SIGTERM app ports, then docker compose stop

Same Cursor tabs: Tasks: Run Task → SB-MS: start all
Each tab runs this script once, so the log stays in the editor chat context.
EOF
}

set_title() {
  printf '\033]0;SB-MS %s\007' "$1"
}

assert_port_free() {
  local port="$1"
  local label="$2"
  if lsof -nP -iTCP:"${port}" -sTCP:LISTEN >/dev/null 2>&1; then
    echo "SB-MS ${label}: port ${port} is already in use." >&2
    lsof -nP -iTCP:"${port}" -sTCP:LISTEN >&2 || true
    exit 1
  fi
}

wait_healthy() {
  local name="$1"
  local i status
  for i in $(seq 1 60); do
    status="$(docker inspect -f '{{.State.Health.Status}}' "${name}" 2>/dev/null || echo missing)"
    if [[ "${status}" == "healthy" ]]; then
      echo "SB-MS ${name} is healthy"
      return 0
    fi
    sleep 1
  done
  echo "SB-MS ${name} did not become healthy (last status: ${status})" >&2
  return 1
}

require_mvn() {
  command -v mvn >/dev/null 2>&1 || {
    echo "SB-MS: mvn is not on PATH" >&2
    exit 1
  }
  if ! java -version 2>&1 | grep -q 'version "21'; then
    echo "SB-MS warning: Java 21 was not detected on PATH. This repo targets Java 21." >&2
  fi
}

load_product_env() {
  local env_file="${ROOT}/.env"
  if [[ ! -f "${env_file}" ]]; then
    echo "SB-MS product-service: missing ${env_file}" >&2
    echo "Expected PRODUCT_DB_URL, PRODUCT_DB_USERNAME, PRODUCT_DB_PASSWORD, SPRING_PROFILES_ACTIVE." >&2
    exit 1
  fi
  set -a
  # shellcheck disable=SC1090
  source "${env_file}"
  set +a
}

run_module() {
  local module="$1"
  shift
  require_mvn
  cd "${SERVICES}"
  mvn -B -pl "${module}" spring-boot:run "$@"
}

ensure_cursor_task_keybinding() {
  local file="${HOME}/Library/Application Support/Cursor/User/keybindings.json"
  if [[ ! -f "${file}" ]]; then
    echo "SB-MS: missing ${file}" >&2
    return 1
  fi
  python3 - "${file}" <<'PY'
import pathlib, sys
path = pathlib.Path(sys.argv[1])
text = path.read_text()
if '"args": "SB-MS: start all"' in text:
    sys.exit(0)
entry = """  {
    "key": "ctrl+shift+cmd+s",
    "command": "workbench.action.tasks.runTask",
    "args": "SB-MS: start all",
    "when": "workspaceFolderBasename == 'SB-MS'"
  }"""
idx = text.rfind("]")
if idx < 0:
    sys.exit("keybindings.json has no closing ]")
head = text[:idx].rstrip()
if head.endswith("["):
    updated = head + "\n" + entry + "\n]\n"
elif head.endswith(","):
    updated = head + "\n" + entry + "\n]\n"
else:
    updated = head + ",\n" + entry + "\n]\n"
path.write_text(updated)
print("SB-MS added Cursor keybinding ctrl+shift+cmd+s → SB-MS: start all")
PY
}

cmd_postgres() {
  set_title "postgres"
  command -v docker >/dev/null 2>&1 || {
    echo "SB-MS: docker is not on PATH" >&2
    exit 1
  }
  echo "SB-MS postgres starting"
  cd "${POSTGRES_DIR}"
  docker compose up -d
  wait_healthy product-postgres
  wait_healthy inventory-postgres
  wait_healthy order-postgres
  echo "SB-MS postgres ready"
  docker compose logs -f --tail=80
}

cmd_discovery() {
  set_title "discovery-server :8761"
  echo "SB-MS starting discovery-server"
  assert_port_free 8761 "discovery-server"
  run_module discovery-server
}

cmd_config() {
  set_title "config-server :8888"
  echo "SB-MS starting config-server"
  echo "SB-MS config repo (relative to the config-server module): ../../infrastructure/config-repo"
  assert_port_free 8888 "config-server"
  run_module config-server
}

cmd_gateway() {
  set_title "api-gateway :8080"
  echo "SB-MS starting api-gateway"
  assert_port_free 8080 "api-gateway"
  run_module api-gateway
}

cmd_order() {
  set_title "order-service :8083"
  echo "SB-MS starting order-service"
  assert_port_free 8083 "order-service"
  run_module order-service
}

cmd_product() {
  set_title "product-service"
  load_product_env
  local profile="${SPRING_PROFILES_ACTIVE:-}"
  local port="8081"
  if [[ "${profile}" == "dev" ]]; then
    port="8182"
  fi
  echo "SB-MS starting product-service"
  echo "SB-MS product env: ${ROOT}/.env  profile=${profile:-<unset>}  expected port ${port}"
  assert_port_free "${port}" "product-service"
  run_module product-service
}

cmd_inventory() {
  local port="${1:-}"
  if [[ ! "${port}" =~ ^[0-9]+$ ]]; then
    echo "Usage: ./scripts/dev-services.sh inventory-service <port>" >&2
    echo "Phase 04 load balancer uses 8091 and 8092." >&2
    exit 1
  fi
  set_title "inventory-service :${port}"
  echo "SB-MS starting inventory-service on port ${port}"
  assert_port_free "${port}" "inventory-service"
  run_module inventory-service -Dspring-boot.run.jvmArguments="-Dserver.port=${port}"
}

cmd_stop() {
  local port pids
  for port in 8761 8888 8080 8081 8182 8082 8083 8091 8092; do
    pids="$(lsof -t -nP -iTCP:"${port}" -sTCP:LISTEN 2>/dev/null || true)"
    if [[ -n "${pids}" ]]; then
      echo "SB-MS stopping port ${port} (pid ${pids//$'\n'/ })"
      # shellcheck disable=SC2086
      kill ${pids} || true
    fi
  done
  if command -v docker >/dev/null 2>&1; then
    echo "SB-MS stopping Postgres containers (data volumes stay)"
    docker compose -f "${POSTGRES_DIR}/docker-compose.yml" stop
  fi
}

cmd_all() {
  local port busy=0
  for port in 8761 8888 8080 8081 8182 8082 8083 8091 8092; do
    if lsof -nP -iTCP:"${port}" -sTCP:LISTEN >/dev/null 2>&1; then
      echo "SB-MS port ${port} is already in use." >&2
      busy=1
    fi
  done
  if [[ "${busy}" -eq 1 ]]; then
    echo "SB-MS run ./scripts/dev-services.sh stop, then ./scripts/dev-services.sh all, so the Cursor tabs can bind these ports." >&2
    exit 1
  fi
  ensure_cursor_task_keybinding
  # A shell cannot create Cursor terminal tabs, and macOS drops synthetic
  # keystrokes without reporting an error. The task has to be started from the editor.
  echo "SB-MS ports are free. Start the Cursor tabs with either:"
  echo "  Ctrl+Shift+Cmd+S"
  echo "  Cmd+Shift+P → Tasks: Run Task → SB-MS: start all"
  echo "Tabs open in order: Postgres, Eureka, Config Server, product, inventory :8091, inventory :8092, order, gateway."
}

main() {
  local cmd="${1:-}"
  case "${cmd}" in
    ""|-h|--help|help) usage ;;
    all) cmd_all ;;
    postgres) cmd_postgres ;;
    discovery-server) cmd_discovery ;;
    config-server) cmd_config ;;
    api-gateway) cmd_gateway ;;
    order-service) cmd_order ;;
    product-service) cmd_product ;;
    inventory-service) cmd_inventory "${2:-}" ;;
    stop) cmd_stop ;;
    *)
      echo "Unknown command: ${cmd}" >&2
      usage >&2
      exit 1
      ;;
  esac
}

main "$@"
