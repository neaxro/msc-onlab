FROM python:bullseye

RUN useradd --create-home \
    --shell /bin/bash \
    -u 1000 \
    auth

WORKDIR /auth

COPY . .

RUN pip install --no-cache-dir -r requirements.txt

USER auth

EXPOSE 5000/tcp

ENTRYPOINT [ "gunicorn", "-w 2", "-b 0.0.0.0:5000", "app:application" ]
