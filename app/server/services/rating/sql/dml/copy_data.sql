\set csvdir `echo $CSVDIR`

\set copy_users '\\copy rating from ' :csvdir 'rating.csv' ' with (format csv);'

:copy_users

ALTER SEQUENCE public.rating_id_seq RESTART 11;
