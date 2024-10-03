#!/bin/bash
# ./buildBootJars.sh
./gradlew clean bootJar

ansible-playbook -i ansible/inventory.ini ansible/uploadJars.yml
# --extra-vars "ansible_user=root" --extra-vars 'ansible_password=<PASSWORD>'