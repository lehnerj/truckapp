#!/bin/bash

sudo yum install -y bc

maxDelayMs=1000
endpoint=<HOST>:8080

# Example requests
# request="http://$endpoint/truckfrontend/release?stallOp=cpu&stallSec=5"
# request="http://$endpoint/truckfrontend/release?stallSec=3"
# request="http://$endpoint/truckfrontend/truck/EMEA-AT-431?stallSec=4"
request="http://$endpoint/truckfrontend/truck/EMEA-AT-431"
while true
do
 ts=$(date +%s%N)
 echo "Executing request $request"
 curl -H "User-Agent: MyCustomUserAgent" -H "Truck-Load-Test: TruckAbc" -H "hey-header: hey" $request
 echo
 elapsedMs=$((($(date +%s%N) - $ts)/1000000))
 if (( elapsedMs < ${maxDelayMs} ));
  then
  sleepTime=$(bc <<< "scale=3; ( ${maxDelayMs} - $elapsedMs ) / 1000")
  echo "Delay next request by $sleepTime ms";
  sleep $sleepTime
 fi
done