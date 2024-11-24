FROM openjdk:17-oracle

WORKDIR /app

COPY target/microservice-friend-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]



#( PS C:\Users\krp77\IdeaProjects\socketserver>  docker build -t microservice_friend .)
#(PS C:\Users\krp77\IdeaProjects\socketserver>  docker run microservice_friend  )

# docker container ls
# docker container ls -a
# docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' fa0ad4b31d4d
# docker image ls
#  docker image rm microservice_friend-f                             (dell image)
# docker start [container]	Запускает данный контейнер.

# docker network create spring-socket-network
# docker ps -a	Перечисляет все контейнеры. Пометка -a показывает как работающие, так и неработающие контейнеры. Чтобы отображать только запущенные контейнеры, эту пометку можно опустить.
# cd  (англ. change directory — изменить каталог) PS C:\Users\krp77> cd "C:\Users\krp77\Music"
#Переход на уровень выше в иерархии директорий:Windows: cd ..
 # Переход в корневую директорию:Windows: cd \


 #  docker tag telegram_bot2 cr.yandex/crp8vchaaorln1absb0a/telegram_bot2:latest
 #  docker push cr.yandex/crp8vchaaorln1absb0a/telegram_bot2:latest
 # docker login  --username oauth  --password y0_AgAAAAAKw1P5AATuwQAAAAETwhY1AABh0d-8IfFNj6jKB0vY_lUm4Wsoqw   cr.yandex
 #  docker image rm b03968f50f0e  -f                             (dell image)
 # docker container ls -a
 # docker container rm d8f248170e98 -f
 # mvn package -DskipTests
