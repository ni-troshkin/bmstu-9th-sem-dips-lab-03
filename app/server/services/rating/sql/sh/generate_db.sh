#!/bin/bash
# This is a comment
sleep 1
PGPASSWORD=$1 psql ratingservice -U dev -a -f /ddl/rating.sql
