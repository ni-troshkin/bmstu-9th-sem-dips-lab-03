#!/bin/bash
# This is a comment
sleep 1
PGPASSWORD=$1 psql libraryservice -U dev -a -f /ddl/library.sql
