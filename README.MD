
# ПРОЕКТ НАХОДИТСЯ В РАЗРАБОТКЕ 

# Стек:
<!-- Основной стек -->
![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=apache-maven&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2_Database-003545?style=flat-square&logo=h2&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit_5-25A162?style=flat-square&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-00A98F?style=flat-square&logo=mockito&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-1B6D5E?style=flat-square&logo=project-lombok&logoColor=white)

<!-- Дополнительная информация -->
![GitHub last commit](https://img.shields.io/github/last-commit/Nitor112221/Codeforces-mashups-configurater?style=flat-square)
# Codeforces mashups configurator
## Проект позволяет с помощью удобных фильтров конфигурировать мэшапы на сайте Codeforces

### Доступные фильтры
    * Задача X-Y из div Z
    * Задачи с рейтингом от X до Y
    * Задачи содержание теги из списка
    * задачи не содержащие теги из списка


### Уже реализовано
    * Модуль работы с базой данных
    * Конфигуратор мэшапов
    * 4 фильтра

### Будет реализовано
    * GUI интерфейс
    * Расширение для браузера
    * Модуль работы в API codeforces

### Информация по кодовой базе
#### В проекте используется in memory база данных H2, в которую при каждом запуске будут подгружаться все данные из API codeforces. Это решение было принято из-за того, как работает API codeforces: в 1 запросе мы получаем сразу всю базу задач из архива/проведённых контестов

## Как запустить

### В данный момент никак. Проект находится на стадии разработки и публичный билд отсутствует. В данный момент реализованны некоторые компоненты и они покрыты тестами, точки входа в программу нет. 