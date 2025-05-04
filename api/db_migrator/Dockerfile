FROM python:3.9.21

WORKDIR /db_migration

COPY . .

RUN pip install --no-cache-dir -r requirements.txt
RUN useradd --create-home \
    --shell /bin/bash \
    -u 1000 \
    migrator \
    && chown -R migrator:migrator migrations/ \
    && chmod -R 755 migrations/

USER migrator

ENTRYPOINT [ "python", "app.py" ]
