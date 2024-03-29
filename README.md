# MonitoringService

## Описание

Monitoring Service - это приложение на базе Spring Boot, которое позволяет пользователям отправлять и просматривать показания, а администраторам просматривать аудиты и управлять типами показаний. Приложение использует пользовательские Spring Boot стартеры для логирования и аудита.

## Функциональность

- Регистрация новых пользователей.
- Аутентификация пользователей.
- Просмотр актуальных показаний.
- Подача показаний.
- Просмотр показаний за конкретный месяц.
- Просмотр истории подачи показаний.
- Аудит действий пользователя (регистрация, авторизация, подача показаний, просмотр показаний).
- Добавление нового типа показаний (только для администраторов).
- Удаление существующего типа показаний (только для администраторов).
- Назначение пользователя администратором (только для администраторов).

## Установка

Для установки проекта выполните следующие шаги:

1. Клонируйте репозиторий: `git clone https://github.com/Vlados4an/Monitoring-Service.git`
2. Перейдите в каталог проекта: `cd Monitoring-Service`
3. Поднимите контейнер базы данных ``` docker compose up -d ```
4. Скачайте и установите пользовательские Spring Boot стартеры, выполнив команду `mvn clean install -U` в каталоге каждого стартера.
5. Соберите проект с помощью Maven: `mvn clean install`

## Использование


Для запуска приложения используйте Spring Boot. Для включения логирования, добавьте аннотацию @EnableLogging в вашем основном классе приложения.

## Технологии

- Java 17
- Spring Boot
- Spring Security
- Spring AOP
- Spring Boot Test
- Maven
- JWT
- TestContainers
- Liquibase
- JdbcTemplate
- PostgreSQL
- Mapstruct
- Docker
- OpenAPI
- Swagger UI
- Custom Spring Boot Starters

## Swagger UI

Swagger UI предоставляет визуальный интерфейс для взаимодействия с API. Вы можете просматривать определения конечных точек API, их параметры, ответы и т.д. Также вы можете отправлять запросы к API прямо из Swagger UI.

Для доступа к Swagger UI перейдите по следующему URL: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Структура базы данных

### `users`

| Колонка   | Тип          | Комментарий                                                                     |
|-----------|--------------|---------------------------------------------------------------------------------|
| id        | bigint       | Уникальный идентификатор пользователя, первичный ключ                           |
| username  | varchar(255) | Имя пользователя для входа в систему, должно быть уникальным                    |
| password  | varchar(255) | Хешированное представление пароля пользователя, используется для аутентификации |
| role      | varchar(255) | Роль пользователя в системе.                                                    |

### `readings`

| Колонка    | Тип          | Комментарий                                                    |
|------------|--------------|----------------------------------------------------------------|
| id         | bigint       | Уникальный идентификатор показания, первичный ключ             |
| username   | varchar(255) | Имя пользователя, которому принадлежит показание, внешний ключ |
| month      | int          | Месяц периода показания, хранится как целое число              |
| year       | int          | Год периода показания, хранится как целое число                |
| heating    | double       | Показание по отоплению за данный период                        |
| cold_water | double       | Показание по холодной воде за данный период                    |
| hot_water  | double       | Показание по горячей воде за данный период                     |

### `audits`

| Колонка  | Тип          | Комментарий                                     |
|----------|--------------|-------------------------------------------------|
| id       | bigint       | Уникальный идентификатор аудита, первичный ключ |
| username | varchar(255) | Имя пользователя, совершившего действие.        |
| timestamp| timestamp    | Время, когда было совершено действие.           |
| action   | varchar(255) | Сохраняет конкретное сообщение об аудите        |

## Тестирование

Проект покрыт юнит-тестами с использованием Spring Boot Test и Testcontainers. Для их запуска откройте класс с тестами, нажмите на название класса правой кнопкой мыши и выберите пункт "Run 'ClassName'".

## Тестовые данные

Приложение инициализируется с предустановленными тестовыми данными для удобства тестирования:

- Админ:
    - Имя пользователя: `admin`
    - Пароль: `admin`
    - Роль: `ADMIN`

- Пользователь для тестирования чтения показаний:
    - Имя пользователя: `test_user`
    - Пароль: `test_pass`
    - Роль: `USER`
    - Показания:
        - Месяц: `1`
        - Год: `2022`
        - Отопление: `100.0`
        - Холодная вода: `200.0`
        - Горячая вода: `300.0`

## Связаться со мной:

- ssvetlaa235@gmail.com
- telegram: [@evlad03](https://t.me/evlad03)