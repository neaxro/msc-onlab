FROM python:bullseye

WORKDIR /auth

COPY . .

RUN pip install --no-cache-dir -r requirements.txt

EXPOSE 5000

ENTRYPOINT [ "python", "app.py" ]
