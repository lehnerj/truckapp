#!/bin/bash
HOST=$1
eval $(ansible-inventory --inventory ansible/inventory.ini --host ${HOST} | jq -r '"ssh -p " + (.ansible_port|tostring) + " " + .ansible_user + "@" + .ansible_host + " -i " + .ansible_ssh_private_key_file')
