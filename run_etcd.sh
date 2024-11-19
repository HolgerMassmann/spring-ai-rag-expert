#!/usr/bin/env bash

docker run -dt -v $(pwd)/etcd:/etcd  quay.io/coreos/etcd:v3.5.5  etcd -advertise-client-urls=http://127.0.0.1:2379 -listen-client-urls http://0.0.0.0:2379 --data-dir /etcd

