# Investment Analyst

## Deploy using Docker Compose
1. Get [Tinkoff Invest API Token](https://tinkoff.github.io/investAPI/token/)
2. Get [YandexGPT API Key & Folder ID](https://yandex.cloud/en/docs/foundation-models/quickstart/yandexgpt). The article suggests using an IAM token, but you can get an [API key for a service account](https://yandex.cloud/en/docs/iam/operations/api-key/create#console_1)
3. Edit .env file to set up your own credentials
4. Run the following
    ```bash
    ./gradlew bootJar
    docker compose up -d
    ```
5. API will be available on http://localhost:8080/api

## Extra
### Swagger UI
The Swagger UI is available on http://localhost:8080/swagger-ui.html
### Monitoring
The application is ready for monitoring with Prometheus and Grafana. You can deploy these services from the docker-compose.yml file in the `monitoring` folder.
