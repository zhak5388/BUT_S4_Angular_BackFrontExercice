
# Create User

http://localhost:8080/swagger-ui/index.html#/user-rest-controller/create

```
curl -X 'POST' \
'http://localhost:8080/api/user' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"firstName": "John",
"lastName": "Smith",
"birthDate": "1999-01-01",  
"email": "john.smith@dummy",
"password": "test!"
}'
```

Response:
```
{
  "id": "660b113bf4bc094c132c2d07",
  "firstName": "John",
  "lastName": "Smith",
  "birthDate": "1999-01-01",
  "email": "john.smith@dummy"
}
```

# Create Tournament

```
curl -X 'POST' \
  'http://localhost:8080/api/tournament' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Tournoi de Paques",
  "startDate": "2024-03-30",
  "endDate": "2024-03-30",
  "maxParticipants": 100,
  "nthPowerEliminationPhase": 3
}'
```

Response:
```
{
"id": "1712001522814",
"name": "Tournoi de Paques",
"createdTimestamp": "2024-04-01T21:58:42.8315637",
"createdByUser": "<<currentUser (security not impl yet)",
"lastUpdatedTimestamp": "2024-04-01T21:58:50.9450682",
"lastUpdatedByUser": "<<currentUser (security not impl yet)"
}
```

# Create Participants

```
curl -X 'POST' \
  'http://localhost:8080/api/tournament/660b1293f4bc094c132c2d09/participants' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "userId": "660b113bf4bc094c132c2d07"
}'
```

Response:
```
{
  "participantId": "660b185c7dff2c478443b288"
}
```


# Close registrations

```
curl -X 'POST' \
'http://localhost:8080/api/tournament/660b1293f4bc094c132c2d09/close-participations' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"comment": "c'\''est fin les inscriptions!"
}'
```

Response:
```
200 OK
```


# Check Registration Closed

```
curl -X 'GET' \
'http://localhost:8080/api/tournament/660b1293f4bc094c132c2d09' \
-H 'accept: */*'
```

Response:
```
{
"id": "660b1293f4bc094c132c2d09",
"name": "Tournoi de Noel",
"state": "RegistrationClose"
}
```

# shuffle Groups, start group hase matches

```
curl -X 'POST' \
'http://localhost:8080/api/tournament/660b1293f4bc094c132c2d09/start-groups-phase' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"groupCount": 50
}'
```

Response:
```
?
```



# Check Group Phase Matches

```
curl -X 'GET' \
'http://localhost:8080/api/tournament/660b1293f4bc094c132c2d09' \
-H 'accept: */*'
```

Response:
```
{
"id": "660b1293f4bc094c132c2d09",
"name": "Tournoi de Noel",
"state": "GroupMatchPhase"
}
```


# compute participant points per groups 

```
curl -X 'GET' \
'http://localhost:8080/api/tournament/660b1293f4bc094c132c2d09/group-points' \
-H 'accept: */*'
```

Response:
```
{
  "tournamentId": "660b1293f4bc094c132c2d09",
  "name": "Tournoi de Noel",
  "groupPts": [
    {
      "groupName": "A",
      "participantPoints": [],
      "remainingGroupMatches": [
        {
          "id": "660b1fef2db1773fbe9f2652",
          "part1": {
            "id": "660b1eb72db1773fbe9f264a",
            "user": {
              "id": "660b1c1c2db1773fbe9f25e5",
              "firstName": "firstname-94",
              "lastName": "lastname-94"
            }
          },
          "part2": {
            "id": "660b1eab2db1773fbe9f2607",
            "user": {
              "id": "660b1c122db1773fbe9f25a2",
              "firstName": "firstname-27",
              "lastName": "lastname-27"
            }
          }
        },
```


# compute participant points per groups for a single participant

```
curl -X 'GET' \
  'http://localhost:8080/api/tournament/660b1293f4bc094c132c2d09/group-points/660b1eb72db1773fbe9f264a' \
  -H 'accept: */*'
```

Response:
```
{
  "groupPts": {
    "participant": {
      "id": "660b1eb72db1773fbe9f264a",
      "user": {
        "id": "660b1c1c2db1773fbe9f25e5",
        "firstName": "firstname-94",
        "lastName": "lastname-94"
      }
    },
    "points": 0,
    "goalAverageSets": 0,
    "goalAveragePoints": 0,
    "remainingMatchCount": 5
  },
  "remainingMatches": [
    {
      "id": "660b1fef2db1773fbe9f2652",
      "part1": {
        "id": "660b1eb72db1773fbe9f264a",
        "user": {
          "id": "660b1c1c2db1773fbe9f25e5",
          "firstName": "firstname-94",
          "lastName": "lastname-94"
        }
      },
      "part2": {
        "id": "660b1eab2db1773fbe9f2607",
        "user": {
          "id": "660b1c122db1773fbe9f25a2",
          "firstName": "firstname-27",
          "lastName": "lastname-27"
        }
      }
    },
```

# Fill a Group Match Score

```
{
  "matchId": "660b1fef2db1773fbe9f2653",
  "part1Id": "660b1eb72db1773fbe9f264a",
  "part2Id": "660b1ea62db1773fbe9f25ed",
  "setScores": [
    { "score1": 11, "score2": 5 },
    { "score1": 13, "score2": 11 },
    { "score1": 11, "score2": 9 }
  ],
  "matchStartTime": "2024-04-01T21:36:26.155Z",
  "matchEndTime": "2024-04-01T21:36:26.155Z",
  "group": "A"
}
```

Response:
```
{
  "participant1Pts": {...},
  "participant2Pts": {...}
}
```
