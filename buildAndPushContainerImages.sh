#!/bin/bash

# Build and Push multi-architecture images for AMD64 and ARM64 (Apple M1)
repoName="docker.io/lehnerj/lehnerj"
for imageName in "spring-sample-truck-frontend" "spring-sample-truck-rest" "spring-sample-truck-backend"
do
  manifestName="${repoName}:${imageName}"
  podman manifest create "${manifestName}"
  podman build --platform linux/amd64,linux/arm64  --manifest "${manifestName}" "${imageName}"
  podman manifest push "${manifestName}"
donere