FROM python:bullseye

RUN useradd --create-home \
    --shell /bin/bash \
    -u 1000 \
    invitation

WORKDIR /invitation

COPY . .

RUN pip install --no-cache-dir -r requirements.txt

USER invitation

EXPOSE 5003/tcp

ENTRYPOINT [ "gunicorn", "-w 2", "-b 0.0.0.0:5003", "app:application" ]
