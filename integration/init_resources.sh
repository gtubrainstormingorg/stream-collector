#!/bin/bash

resource_docker_file=$1

export PYTHONWARNINGS="ignore:Unverified HTTPS request"

sbt "project $flavour; set Docker / version := \"0.0.0\"" docker:publishLocal

cd integration && docker-compose -f "$resource_docker_file" up -d

sleep 15