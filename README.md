# my-blog-backend-app

### Сборка проекта

В проекте предусмотрен maven wrapper. Поэтому для сборки исходников в war необходимо выполнить следующее

```bash
#Linux/MacOS
./gradlew clean bootJar 

#Windows
gradlew.bat clean bootJar
```

Далее в корне проекта появится каталог build, внутри которого в папке libs лежит файл с расширением jar

### Запуск исполняемого файла проекта

Запускаем файл командой из корня проекта

```bash
#Linux/MacOS
java -jar ./build/libs/my-blog-backend-app-0.0.1-SNAPSHOT.jar

#Windows
java -jar build\libs\my-blog-backend-app-0.0.1-SNAPSHOT.jar

```

### Тестирование

Запуск всех тестов

```bash
#Linux/MacOS
./gradlew clean test

#Windows
./gradlew clean test
```

В проекте предусматривается модульное и интеграционное тестирование. Соответствующие тесты помечены тегами `unit` и
`integration`. Интеграционные соответственно подразделяются на слои `service`, `dao` и `rest`. Юнит тесты подразделяются
на `service` и `rest`. Для запуска нужных типов тестов нужно указать параметр и перечислить нужные теги через запятую.
А для исключения необходимо также через запятую прописать нужные теги, но указав `exclude:` перед ними.

```bash
#Linux/MacOS
./gradlew clean test -Ptags=unit
./gradlew clean test -Ptags=rest,service  
./gradlew clean test -Ptags=exclude:integration
./gradlew clean test

#Windows
gradlew.bat clean test -Ptags=unit
gradlew.bat clean test -Ptags=rest
gradlew.bat clean test -Ptags=dao
gradlew.bat clean test -Ptags=exclude:dao,rest
```