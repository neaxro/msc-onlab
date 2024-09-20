packages:
	pip install -r api/requirements.txt

build_test:
	docker build -t local-testing:latest .

build:
	docker build -t msc-onlab-backend:latest .

test:
	pytest api/

coverage:
	coverage run -m pytest api/

up:
	./run.sh

dup:
	# Run container
	docker run -d \
		-p 5000:5000 \
		--name msc_onlab \
		-e FLASK_ENV="development" \
		-e MONGODB_CONNECTION_URL="mongodb://192.168.1.68:27017" \
		local-testing

ddown:
	docker stop msc_onlab && docker rm msc_onlab
