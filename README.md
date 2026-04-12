# my-blog-backend-app

### Сборка проекта

В проекте предусмотрен maven wrapper. Поэтому для сборки исходников в war необходимо выполнить следующее

```bash
#Linux/MacOS
./mvnw clean package

#Windows
mvnw.cmd clean package
```

Далее в корне проекта появится каталог target, внутри него ROOT.war. Проект собран и готов к деплою в сервлет-контейнер.

### Деплой в Tomcat

Для развертывания бэкенда необдходим сервлет-контейнер Tomcat выше версии 9. Специфика реализации интерфейса блога
предполагает обращение к бэкенду по адресу `http://localhost:8080/`, поэтому архив имеет наименование ROOT.war.
Необходимо перейти в каталог Tomcat в папку webapps и удалить/изменить наименование каталога ROOT на другое (например
ROOT_BKP). Далее поместить туда собраный ROOT.war из проекта. Приложение автоматически развёртывается на корневом URL.
Для обновления бэкенда необходимо пересобрать и также поместить в webapps новый архив, заменив старый.

### Тестирование

Запуск всех тестов

```bash
#Linux/MacOS
./mvnw clean test

#Windows
mvnw.cmd clean test
```

В проекте предусматривается модульное и интеграционное тестирование. Соответствующие тесты помечены тегами `unit` и
`integration`. Интеграционные соответственно подразделяются на слои `dao` и `rest`. Поэтому для запуска того или иного
типа можно использовать следующую команды

```bash
#Linux/MacOS

./mvnw clean test -Dgroups=unit
./mvnw clean test -Dgroups=integration 
./mvnw clean test -Dgroups=dao
./mvnw clean test -Dgroups=rest

#Windows
mvnw.cmd clean test -Dgroups=unit
mvnw.cmd clean test -Dgroups=integration 
mvnw.cmd clean test -Dgroups=dao
mvnw.cmd clean test -Dgroups=rest
```