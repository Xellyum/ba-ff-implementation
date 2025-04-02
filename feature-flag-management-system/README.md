# feature-flag-management-service

## Running the application in dev mode
```shell script
   mvn quarkus:dev
```
> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

---

## Packaging and running the application

##### The application can be packaged using:
```shell script
   mvn package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.

##### The application can be run using:
```shell script
   docker compose up -d
```

---

## REST Endpoints

### Get Feature Flags
**GET** `/flag`

**Query Parameters:**

| Name      | Type   | Description                    |
|-----------|--------|--------------------------------|
| `env`     | string | Environment filter (optional)  |
| `feature` | string | Feature name filter (optional) |


### Create new Feature Flag
**POST** `/flag`

**Request Body (JSON):**
```json
{
  "feature": "new-feature",
  "active": true,
  "environments": ["DEVELOPMENT", "PRODUCTION"],
  "description": "A new feature flag",
  "createdAt": "2022-03-10T16:15:50Z",
  "lastUsed": "2022-03-10T16:15:50Z",
  "dependencies": ["other-feature"]
}
```

### Edit Feature Flag
**PUT** `/flag`

**Request Body (JSON):**
```json
{
  "feature": "new-feature",
  "active": true,
  "environments": ["DEVELOPMENT", "PRODUCTION"],
  "description": "A new feature flag",
  "createdAt": "2022-03-10T16:15:50Z",
  "lastUsed": "2022-03-10T16:15:50Z",
  "dependencies": ["other-feature"]
}
```

### Delete Feature Flag
**DELETE** `/flag`

**Path Parameters:**

| Name       | Type   | Description  |
|------------|--------|--------------|
| `/feature` | string | Feature name |

### Create Feature Flag Event (Testing)
**POST** `/flag/event`

---

## Kafka commands for debugging

### Kafka (bitnami) Commands
display all topics
```shell script
   docker exec -it feature-flag-management-service-kafka-1 kafka-topics.sh --bootstrap-server localhost:9092 --list
```

display messages published to topic  (development-flags)
```shell script
   docker exec -it feature-flag-management-service-kafka-1 kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic development-flags --from-beginning
```

```shell script
   docker exec -it feature-flag-management-service-kafka-1 kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic development-flagss
```

### Kafka (Confluentinc) Commands
Display all topics
```shell script
   docker exec -it ffms-kafka /usr/bin/kafka-topics --list --bootstrap-server localhost:9093
```

Display all messages of a topic
```shell script
   docker exec -it ffms-kafka /usr/bin/kafka-console-consumer --bootstrap-server localhost:9093 --topic dev-flags --from-beginning
```

Create new message for a topic
```shell script
   echo "Hello, Kafka!" | docker exec -i ffms-kafka /usr/bin/kafka-console-producer --broker-list localhost:9093 --topic dev-flags
```
