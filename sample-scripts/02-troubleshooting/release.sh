#!/bin/bash


timeNowMs=$(($(date +%s)*1000))

# Release API
curl --request POST \
--url <URL>/api/releases \
--header 'Content-Type: application/json' \
--header 'authorization: apiToken <TOKEN>' \
--data '{
"applications": [
{
"name": "<APP-NAME>"
}
],
"name": "1st release",
"start": 'timeNowMs'
}'