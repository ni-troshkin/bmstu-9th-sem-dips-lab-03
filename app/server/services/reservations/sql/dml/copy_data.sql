\set csvdir `echo $CSVDIR`

\set copy_reservation '\\copy reservation from ' :csvdir 'reservation.csv' ' with (format csv);'

:copy_reservation

ALTER SEQUENCE public.reservation_id_seq RESTART 16;
