# Investment Analyst

## Deploy using Docker Compose
1. Get [Tinkoff Invest API Token](https://tinkoff.github.io/investAPI/token/) and [OpenAI API Key](https://platform.openai.com/api-keys)
2. Edit .env file to set up your own credentials
3. Run the following
    ```bash
    ./gradlew bootJar
    docker compose up -d
    ```
4. Site will be available on http://localhost:8080

## Extra
### Swagger UI
The Swagger UI is available on http://localhost:8080/swagger-ui.html
### Monitoring
The application is ready for monitoring with Prometheus and Grafana. You can deploy these services from the docker-compose.yml file in the `monitoring` folder.
