\set csvdir `echo $CSVDIR`

\set copy_library '\\copy library from ' :csvdir 'library.csv' ' with (format csv);'

\set copy_books '\\copy books from ' :csvdir 'books.csv' ' with (format csv);'

\set copy_library_books '\\copy library_books from ' :csvdir 'library_books.csv' ' with (format csv);'

:copy_library

:copy_books

:copy_library_books

ALTER SEQUENCE public.library_id_seq RESTART 11;
ALTER SEQUENCE public.books_id_seq RESTART 21;
