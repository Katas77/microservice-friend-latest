# Командный проект курса «Java-разработчик – Социальная сеть»
###  В основе проекта лежит Микросервисная архитектура стека Java-технологий 
### Используемые технологии
- Spring Boot 2.7
- Maven 3
- Lombok
- Mapstruct
- Liquibase
- PostgreSQL
- Nimbus-jose-jwt
- Openfeign
- Spring-kafka

## Требования

### JDK 17
Проект использует синтаксис Java 17. 
### Docker
Для запуска проекта вам потребуется установленный и запущенный Docker.
Для запуска БД(PostgreSQL) требется запустить соответствующий сервис в Docker.

##   Сервис отвечает за взаимодействия между пользователями социальной сети.
- Оправку запроса на добавления в друзья
-  Подтверждение дружбы и добавление других пользователей в список друзей
-	Удаление  из друзей
-	Подписку на пользователя
-	Есть возможность блокировать пользователей
- Реализована логика рекомендации возможных друзей. В основе логики лежит рекомендация тех пользователей социальной сети,  у которых два и более общих друзей. Возможно изменение параметра.


### Запуск контейнера с базой данных

```bash
mvn package 
docker build -t romakat77/microservice-friend:latest .
docker login
docker push romakat77/microservice-friend:latest
docker pull romakat77/microservice-friend:latest
docker run romakat77/microservice-friend:latest
docker run --name friend2 -p 8087:8087 romakat77/microservice-friend:latest
```


### IntelliJ IDEA

Запустите main метод класса Application


## Database:
- Postgresql


![image](./swagger/1.jpg )