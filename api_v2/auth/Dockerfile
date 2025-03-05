FROM python:bullseye

WORKDIR /auth

COPY . .

RUN pip install --no-cache-dir -r requirements.txt

EXPOSE 5000/tcp

ENTRYPOINT [ "gunicorn", "-w 2", "-b 0.0.0.0:5000", "app:application" ]
