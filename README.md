# Sample three tier Spring Boot app + 2 databases

TruckFrontend -> TruckREST -> TruckBackend -> MySQL

Not production code, only for demo purpose.

Uses plain JDBC intentionally, not Spring JDBC nor Hibernate

## Prerequisites
Install Podman on your local machine only if you want to run the app locally on your notebook / desktop machine:
* [Install Podman](https://podman.io/docs/installation)
* or install [Podman Desktop](https://podman-desktop.io) with a powerful UI if you want to run the app on your local machine, e.g. macOS

Install Ansible on your local machine if you want to run the app on a remote machine:
* [Install Ansible](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html#intro-installation-guide)


## Run the microservices app manually via Podman (local or remote machine deployment)

### Run MySQL TruckDB with Podman
```
mkdir ~/mysql_truckdb_data
sudo podman run --name mysqltruckdb -p 8090:3306 -v ~/mysql_truckdb_data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD='password' -e MYSQL_USER=dbuser -e MYSQL_PASSWORD='password' -e MYSQL_DATABASE=truckdb mysql:latest
```

### Run MySQL AuditDB with Podman
```
mkdir ~/mysql_auditdb_data
sudo podman run --name mysqlauditdb -p 8091:3306 -v ~/mysql_auditdb_data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD='password' -e MYSQL_USER=dbuser -e MYSQL_PASSWORD='password' -e MYSQL_DATABASE=auditdb mysql:latest
```

### Run Spring Boot Apps with Podman
```
podman run -p 8080:8080 -e TRUCK_TARGETENDPOINT=host.containers.internal:8081 --rm "docker.io/lehnerj/lehnerj:spring-sample-truck-frontend"
podman run -p 8081:8081 -e TRUCK_TARGETENDPOINT=host.containers.internal:8082 -e SPRING_DATASOURCE_URL="jdbc:mysql://host.containers.internal:8091/auditdb" --rm "docker.io/lehnerj/lehnerj:spring-sample-truck-rest"
podman run -p 8082:8082 -e SPRING_DATASOURCE_URL="jdbc:mysql://host.containers.internal:8090/truckdb" --rm "docker.io/lehnerj/lehnerj:spring-sample-truck-backend"
```


## Run the microservices app via Ansible (remote machine deployment)

### Prerequisites to run the microservices app
1. Provision a new Red Hat Linux virtual machine (one machine is enough for the whole application)
2. Update [inventory.ini](ansible/inventory.ini) with the ansible_host, ip, user, ...
3. Accept the SSH fingerprint (e.g. via ssh or login via [./sshToHost.sh](sshToHost.sh) frontend and logout again)
4. Setup remote machine, installs Podman (via Ansible Playbook): `ansible-playbook -i ansible/inventory.ini ansible/setupLinux.yml`

### Run Spring Boot apps via Ansible playbook
`ansible-playbook -i ansible/inventory.ini ansible/runImages.yml`


## Run the microservices app manually via java -jar (local or remote machine deployment)
You can run the Truck microservices app locally on your notebook / desktop or remotely on a Linux machine.

### Run MySQL TruckDB with Podman
```
mkdir ~/mysql_truckdb_data
sudo podman run --name mysqltruckdb -p 8090:3306 -v ~/mysql_truckdb_data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD='password' -e MYSQL_USER=dbuser -e MYSQL_PASSWORD='password' -e MYSQL_DATABASE=truckdb mysql:latest
```

### Run MySQL AuditDB with Podman
```
mkdir ~/mysql_auditdb_data
sudo podman run --name mysqlauditdb -p 8091:3306 -v ~/mysql_auditdb_data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD='password' -e MYSQL_USER=dbuser -e MYSQL_PASSWORD='password' -e MYSQL_DATABASE=auditdb mysql:latest
```

### Startup Spring Boot apps with java -jar

#### Local deployment: Build Jars with Gradle (only necessary for local machine deployment)
Build the artifacts locally if you want to run the app on your local machine.
`./gradlew clean build`
Create the symlinks
`./createSymlinks.sh`

#### Remote deployment: Upload Jars with Ansible (only necessary for remote machine deployment)
Run Ansible on your local machine:
```
# If inventory contains ssh-key
ansible-playbook -i ansible/inventory.ini ansible/uploadJars.yml
# If you want to override user/password
# ansible-playbook -i ansible/inventory.ini ansible/uploadJars.yml --extra-vars "ansible_user=root" --extra-vars 'ansible_password=<PASSWORD>'
```

#### Run Frontend app
Optional: For remote machine deployment login via `./sshToHost.sh frontend`

```
# Frontend-1
TRUCK_TARGETENDPOINT=localhost:8081 jre17/bin/java -jar spring-sample-truck-frontend-0.0.1-SNAPSHOT.jar
# Optional startup an additional frontend
# TRUCK_TARGETENDPOINT=localhost:8081 jre17/bin/java -Dserver.port=10000 -jar spring-sample-truck-frontend-0.0.1-SNAPSHOT.jar
```

#### Run Rest (facade) app
Optional: For remote machine deployment login via `./sshToHost.sh restfacade`
```
TRUCK_TARGETENDPOINT=localhost:8082 SPRING_DATASOURCE_URL="jdbc:mysql://localhost:8091/auditdb" jre17/bin/java -jar spring-sample-truck-rest-0.0.2-SNAPSHOT.jar
```

#### Backend app
Optional: For remote machine deployment login via `./sshToHost.sh backend`
```
SPRING_DATASOURCE_URL="jdbc:mysql://localhost:8090/truckdb" jre17/bin/java -jar spring-sample-truck-backend-0.0.3-SNAPSHOT.jar
```


## Execute Requests
```
endpoint=localhost:8080
request="http://$endpoint/truckfrontend/release"
curl -H "User-Agent: MyCustomUserAgent" $request;
```

Also see [loadgenerator.sh](sample-scripts/00-general/loadgenerator.sh)


## Known issues
* Manual changes are required to run the app in a distributed setup
  * TRUCK_TARGETENDPOINT & SPRING_DATASOURCE_URL must be adjusted manually if you setup the app on multiple machines


## Workarounds
* Login to docker.io (Docker Hub) to not get into the pull limit: `podman login docker.io`
* [runImages.yml](ansible/runImages.yml) can be run locally too if you uncomment all lines with `# delegate_to: 127.0.0.1`