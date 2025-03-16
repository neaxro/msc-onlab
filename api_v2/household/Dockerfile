FROM python:bullseye

WORKDIR /household

COPY . .

RUN pip install --no-cache-dir -r requirements.txt

EXPOSE 5001/tcp

ENTRYPOINT [ "gunicorn", "-w 2", "-b 0.0.0.0:5001", "app:application" ]
