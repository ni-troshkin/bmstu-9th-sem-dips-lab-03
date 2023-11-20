#!/bin/bash
# This is a comment
sleep 1
PGPASSWORD=$1 CSVDIR=/data/ psql reservationservice -U dev -a -f /dml/copy_data.sql

