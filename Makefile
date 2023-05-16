up:
	gradle clean build && docker compose up -d

down:
	docker compose down && docker image prune -af
