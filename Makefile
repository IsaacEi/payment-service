setup-env:
	cp -n .env.example .env || true

up: setup-env
	docker compose up --build

down:
	docker compose down

down-clean:
	docker compose down -v

init-db:
	docker compose run --rm sqlserver-init
