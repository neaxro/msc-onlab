# Install required Python packages from requirements.txt
packages:
	pip install -r api/requirements.txt

# Build Docker image for testing
build_test:
	docker build -t local-testing:latest .

# Build Docker image for production
build:
	docker build -t msc-onlab-backend:latest .

# Run tests with pytest
test:
	pytest api/

# Run tests with coverage
coverage:
	coverage run -m pytest api/

# Bring up services using Docker Compose
up:
	docker compose up

# Bring up the API natively (without Docker)
api_native_up:
	./run.sh

# Bring up the API with Docker, using a container
api_docker_up:
	# Run container
	docker run -d \
		-p 5000:5000 \
		--name msc_onlab \
		-e FLASK_ENV="development" \
		-e MONGODB_CONNECTION_URL="mongodb://localhost:27017" \
		local-testing

# Stop and remove the Docker container for the API
api_docker_down:
	docker stop msc_onlab && docker rm msc_onlab
