# Sample three tier Spring Boot app + 2 databases

TruckFrontend -> TruckREST -> TruckBackend -> MySQL

Not production code, only for demo purpose.

Uses plain JDBC intentionally, not Spring JDBC nor Hibernate

## Run MySQL TruckDB
```
mkdir ~/mysql_truckdb_data
sudo yum -y install podman
# Optional: podman login docker.io
sudo podman run --name mysqltruckdb -p 8090:3306 -v ~/mysql_truckdb_data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD='password' -e MYSQL_USER=dbuser -e MYSQL_PASSWORD='password' -e MYSQL_DATABASE=truckdb mysql:latest
```

## Run MySQL AuditDB
```
mkdir ~/mysql_auditdb_data
sudo podman run --name mysqlauditdb -p 8091:3306 -v ~/mysql_auditdb_data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD='password' -e MYSQL_USER=dbuser -e MYSQL_PASSWORD='password' -e MYSQL_DATABASE=auditdb mysql:latest
```

## Inspect MySQL database
```
podman exec -it mysql mysql -p
enter password: password
use truckdb;
show tables;
...

podman exec -it mysql mysql -p
use auditdb;
select * from audit;
+---------------------------------------------------------------+------------------------------------------------+
| auditid                                                       | msg                                            |
+---------------------------------------------------------------+------------------------------------------------+
| AUDIT-2024-08-16-12:44:29154fcd45-bea7-4cd9-81ed-4bb92120c500 | getAllTrucksFromRegion: region=EMEA country=AT |
| AUDIT-2024-08-16-12:44:3243aa649e-2ccf-4005-98fe-e7ea109085ba | getAllTrucksFromRegion: region=EMEA country=AT |
| AUDIT-2024-08-16-12:44:34827d679b-149b-48cd-b6b8-761fd9940103 | getAllTrucksFromRegion: region=EMEA country=AT |
```

## Ansible

```
ssh root@ip

---

# If inventory contains ssh-key
ansible-playbook -i ansible/inventory.ini ansible/uploadJars.yml
# If you want to override user/password
ansible-playbook -i ansible/inventory.ini ansible/uploadJars.yml --extra-vars "ansible_user=root" --extra-vars 'ansible_password=<PASSWORD>'
```

---

```
sudo yum install -y tmux
```

---

## Startup Spring Boot Apps
### Frontend
```
# Frontend-1
TRUCK_TARGETENDPOINT=localhost:8081 jre17/bin/java -jar truckfrontend-0.0.1-SNAPSHOT.jar
# Optional Frontend-2
TRUCK_TARGETENDPOINT=localhost:8081 jre17/bin/java -Dserver.port=10000 -jar truckfrontend-0.0.1-SNAPSHOT.jar
```

### Rest
```
TRUCK_TARGETENDPOINT=localhost:8082 SPRING_DATASOURCE_URL="jdbc:mysql://localhost:8091/auditdb" jre17/bin/java -jar truckrest-0.0.2-SNAPSHOT.jar
```

### Backend
```
SPRING_DATASOURCE_URL="jdbc:mysql://localhost:8090/truckdb" jre17/bin/java -jar truckbackend-0.0.3-SNAPSHOT.jar
```

## Execute Requests
```
# Check release every 100ms
while true; do curl -H "User-Agent: MyCustomUserAgent" -H "JLHeader: Josef" -H "hey-header: hey" $request; sleep 0.1; done
```

Also see [loadgenerator.sh](sample-scripts/00-general/loadgenerator.sh)