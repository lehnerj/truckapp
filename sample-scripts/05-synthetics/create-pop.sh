#!/bin/bash

sudo helm --kubeconfig /etc/rancher/k3s/k3s.yaml install synthetic-pop \
    --repo https://agents.instana.io/helm  \
    --namespace syn \
    --create-namespace \
    --set downloadKey="<DOWNLOAD_KEY>" \
    --set controller.location="TruckPoP;My Truck PoP;Austria;Vienna;0;0;This is a testing Synthetic Point of Presence" \
    --set controller.instanaKey="<DOWNLOAD_KEY>" \
    --set controller.instanaSyntheticEndpoint="https://synthetics-green-saas.instana.io" \
    --set redis.tls.enabled=false \
    --set redis.password="<PASSWORD>" \
    synthetic-pop