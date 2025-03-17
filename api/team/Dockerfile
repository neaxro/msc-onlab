FROM python:bullseye

RUN useradd --create-home \
    --shell /bin/bash \
    -u 1000 \
    team

WORKDIR /team

COPY . .

RUN pip install --no-cache-dir -r requirements.txt

USER team

EXPOSE 5001/tcp

ENTRYPOINT [ "gunicorn", "-w 2", "-b 0.0.0.0:5001", "app:application" ]
