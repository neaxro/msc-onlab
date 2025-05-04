FROM postgres:17.4

ENV POSTGRES_DB=root
ENV POSTGRES_USER=root
ENV POSTGRES_PASSWORD=pass

COPY init.sql /docker-entrypoint-initdb.d/

EXPOSE 5432
