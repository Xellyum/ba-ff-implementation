# Setup

## Start FFMS
```shell script
  docker compose -f feature-flag-management-service/docker-compose.yml up -d
```

## Start NC
```shell script
  docker compose -f nc/docker-compose.yml up -d
```

## Start PWS
```shell script
  docker compose -f pws/docker-compose.yml up -d
```