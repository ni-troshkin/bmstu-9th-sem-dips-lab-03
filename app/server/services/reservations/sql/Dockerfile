FROM postgres:14.5

ENV CSVDIR /data/lib/postgresql/csvs/

COPY ./data/ ${CSVDIR}

COPY ./ddl/reservation.sql /docker-entrypoint-initdb.d/01_reservation.sql
COPY ./dml/copy_data.sql /docker-entrypoint-initdb.d/02_copy_data.sql
