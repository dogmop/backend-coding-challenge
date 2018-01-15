#!/usr/bin/env bash
echo 'Building docker image ...'
docker build -t engage_expenses .
docker run -d -p 8080:8080 engage_expenses