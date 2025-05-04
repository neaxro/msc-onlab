FROM python:bullseye

RUN useradd --create-home \
    --shell /bin/bash \
    -u 1000 \
    task

WORKDIR /task

COPY . .

RUN pip install --no-cache-dir -r requirements.txt

USER task

EXPOSE 5002/tcp

ENTRYPOINT [ "gunicorn", "-w 2", "-b 0.0.0.0:5002", "app:application" ]
