#!/bin/bash
# This is a comment
sleep 1
PGPASSWORD=$1 psql reservationservice -U dev -a -f /ddl/reservation.sql
