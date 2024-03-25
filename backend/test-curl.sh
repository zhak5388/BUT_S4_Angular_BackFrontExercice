#!/usr/bin/env bash
#set -x

baseApiUrl="http://localhost:8080/api"
acceptJson="Accept:application/json"
contentTypeJson="Content-type:application/json"
getJsonHeaders=(-H "Accept:application/json")
postJsonHeaders=(-H "Accept:application/json" -H "Content-type:application/json")

function doPost() {
  subPath=$1
  body=$2
  echo "# curl "${baseApiUrl}${subPath}" -X POST ${postJsonHeaders[@]} --data-binary ${body}"
  # curl "${baseApiUrl}${subPath}" -X POST ${postJsonHeaders[@]} --data-binary "${body}"
  curl "${baseApiUrl}${subPath}" -X POST -H ${acceptJson} -H ${contentTypeJson} --data-binary "${body}"
}

function doGet() {
  subPath=$1
  echo "#" curl "${baseApiUrl}${subPath}" ${getJsonHeaders[@]}
  curl "${baseApiUrl}${subPath}" ${getJsonHeaders[@]}
}


body=$( cat <<EOF
{
  "firstName": "Arnaud",
  "lastName": "Nauwynck",
  "birthDate": "1974-01-24",
  "email": "arnaud.nauwynck@gmail.com",
  "password": "test!"
}
EOF
)
# doPost "/user" "${body}"


body=$( cat <<EOF
{
  "firstName": "Jean",
  "lastName": "Dupont",
  "birthDate": "2000-01-01",
  "email": "jean.dupont@dummy",
  "password": "test!"
}
EOF
)
# doPost "/user" "${body}"

body=$( cat <<EOF
{
  "firstName": "John",
  "lastName": "Smith",
  "birthDate": "1999-01-01",
  "email": "john.smith@dummy",
  "password": "test!"
}
EOF
)
# doPost "/user" "${body}"
userId_JohnSmith="660b113bf4bc094c132c2d07"



body=$( cat <<EOF
{
  "name": "Tournoi de Noel",
  "startDate": "2024-12-24",
  "endDate": "2024-12-24",
  "maxParticipants": 100,
  "nthPowerEliminationPhase": 3
}
EOF
)
# doPost "/tournament" "${body}"

tournamentId="660b1293f4bc094c132c2d09"

for i in $( seq 1 100 ); do
    # echo "creating user-$i"
    body=$( cat <<EOF
{
  "firstName": "firstname-${i}",
  "lastName": "lastname-${i}",
  "birthDate": "1990-01-01",
  "email": "firstname-${i}.lastname-${i}@dummy",
  "password": "test!"
}
EOF
)
    # doPost "/user" "${body}"
  done

echo "register participants (from csv file)"
cat db/tournamentDb.user.csv | while read line; do
    userId=$(echo $line | cut -f1 -d,)
    # echo "adding participant for userId: $userId"
    body=$( cat <<EOF
{
  "userId": "${userId}"
}
EOF
)
    # doPost "/tournament/${tournamentId}/participants" "${body}"
  done


body=$( cat <<EOF
{
  "comment": "c'est fini les inscriptions!"
}
EOF
)
# doPost "/tournament/${tournamentId}/close-participations" "${body}"



body=$( cat <<EOF
{
  "matchId": "660b1fef2db1773fbe9f2652",
  "part1Id": "660b1eb72db1773fbe9f264a",
  "part2Id": "660b1eab2db1773fbe9f2607",
  "setScores": [
    { "score1": 11, "score2": 5 },
    { "score1": 8, "score2": 11 },
    { "score1": 13, "score2": 11 },
    { "score1": 11, "score2": 9 }
  ],
  "matchStartTime": "2024-04-01T21:19:02.353Z",
  "matchEndTime": "2024-04-01T21:19:02.353Z",
  "group": "A"
}
EOF
)
doPost "/tournament/${tournamentId}/fill-group-match-score" "${body}"