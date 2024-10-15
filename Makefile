-include .env
# Define the Docker Compose command for convenience
DCOMPOSE=docker-compose

# Target to build and start the dev environment
load-env:
	source .env
dev: load-env
	$(DCOMPOSE) down -v
	$(DCOMPOSE) up --build

# Target to stop the containers
stop:
	$(DCOMPOSE) down

# Target to show logs
logs:
	$(DCOMPOSE) logs -f

# Target to restart the app only
restart-app:
	$(DCOMPOSE) restart app

pre-test:
	docker-compose -f docker-compose.test.yml down --remove-orphans

test: pre-test
	docker compose -f docker-compose.test.yml up --build --abort-on-container-exit