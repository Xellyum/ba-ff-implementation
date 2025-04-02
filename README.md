# Setup

## Start FFMS
```shell script
  docker compose -f feature-flag-management-system/docker-compose.yml up -d
```

## Start NC
```shell script
  docker compose -f network-cache/docker-compose.yml up -d
```

## Start PWS
```shell script
  docker compose -f production-workflow-simulator/docker-compose.yml up -d
```