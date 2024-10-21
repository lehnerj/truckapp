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
