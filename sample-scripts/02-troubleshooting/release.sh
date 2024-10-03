#!/bin/bash


timeNowMs=$(($(date +%s)*1000))

# Release API
curl --request POST \
--url https://sesandbox-instana.instana.io/api/releases \
--header 'Content-Type: application/json' \
--header 'authorization: apiToken <TOKEN>' \
--data '{
"applications": [
{
"name": "JLTruck"
}
],
"name": "1st release",
"start": 'timeNowMs'
}'