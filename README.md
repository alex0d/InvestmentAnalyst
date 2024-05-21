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


### Swagger UI
The Swagger UI is available on http://localhost:8080/swagger-ui.html