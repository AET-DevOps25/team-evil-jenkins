FROM postgres:15

ENV POSTGRES_USER=admin
ENV POSTGRES_PASSWORD=your_secure_password

EXPOSE 5432

COPY ./init.sql /docker-entrypoint-initdb.d/
VOLUME /var/lib/postgresql/data