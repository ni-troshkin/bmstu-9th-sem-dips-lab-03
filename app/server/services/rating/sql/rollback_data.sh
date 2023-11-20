#!/bin/bash
# get_data
docker exec database sh -c "/sh/rollback_data.sh $1"
