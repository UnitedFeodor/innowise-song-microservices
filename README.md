# Song Enricher

## Сборка и запуск
-Клонировать репозиторий
-Выполнить команду **`./mvnw clean package`**
-Выполнить команду **`docker-compose up`**

## Аутентификация
-Отправить GET запрос 
http://localhost:9000/oauth2/authorize?response_type=code&client_id=song-enricher-client&redirect_uri=http://localhost:9000/login&scope=openid%20profile
(вместо localhost - релевантный адрес auth api)
-Залогиниться в форме Spring Security как user или admin (пароль совпадает с юзернеймом)
-Полученный в строке запроса код вместе с остальными параметрами отправить POST запросом на 
http://localhost:9000/oauth2/token 
(вместо localhost - релевантный адрес auth api): 
    grant_type=authorization_code
    scope=openid profile
    code=${RECEIVED_CODE}
    redirect_uri=${CONFIGURED_URL}
    client_id=song-enricher-client
-Полученный access token прикреплять как Bearer в хэдере запросов

## Задание
Необходимо реализовать изображённую на схеме микросервисную архитектуру

1. Загружаем файл песни через File API, сам файл кладём в S3 либо локально, если вдруг S3 отвалился, 
в БД кладём имя файла, тип стораджа (S3/local) и путь в этом сторадже 
2. После загрузки, отправляем ID файла в очередь, enricher service консьюмит айдишку из сообщения, скачивает файл, 
парсит метаданные, вытаскивает из спотифая всякие данные по типу автора, альтернативного названия трека, длительность 
и тд (на что хватит фантазии) и отправляет в другую очередь 
3. Song service консьюмит метаданные и сохранят в свою БД. 
Экспозит CRUD эндпоинты. Например, возвращает по запросу метаданные трека и айди файла, который можно вытащить через File API 
4. Все запросы к сервису делаются через Spring Cloud Gateway, сервис дискавери через Eureka, всё это деплоится в docker compose. 
Альтернативно, для тех, кто уже смешарик, можно использовать кубер, тогда будет Ingress Controller, 
а сервис дискавери работает из коробки 
5. Аутентификация/авторизация через Auth API - валидный, подписанный JWT отправляется клиенту, зашитые внутри права 
чекаются в необходимых сервисах. Права можно декомпозировать как угодно, например для обычных пользователей это 
просто чтение, а для админов модификация 
6. В Enricher service для работы с очередями и Spotify API использовать Apache Camel, то же самое для работы 
с очередью в Song API 
7. Фронтенд любой - из того, что сверху писал Миша, без привязки к группе. Но я лично предпочитаю Angular =) 
8. Помимо юнит и интеграционных тестов, желательно иметь end-to-end тесты, можно просто бэк 

Стек: Spring Boot, 
СУБД любые (можно даже NoSQL), localstack, Apache Camel, testcontainers, resilience4j (например), 
а дальше полёт фантазии 
Также использовать паттерны: circuit breaker distributed tracing (логи складывать в cloudwatch) 
externalized configuration (использовать spring cloud config)
![microservice structure.png](microservice%20structure.png)