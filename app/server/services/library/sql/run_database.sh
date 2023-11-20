#!/bin/bash
# copy db data
docker cp . database:/

# generate tables
docker exec database sh -c "/sh/generate_db.sh $1"

