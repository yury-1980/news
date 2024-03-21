# News - система управления новостями

#### Описание:

##### Проект создан с использованием java 17.

Настройки находятся в файлах "yaml" по адресу https://github.com/yury-1980/finale-config.git .
#### В проекте реализованы
1. CRUD операции, синхронизированные с кешем, на основе алгоритма LRU и LFU, через аспект(aspectj).
2. автоматическое создание и заполнение данными таблиц, с помощью Liquibase.
3. Подключён Swagger(OPEN API) 1. http://localhost:8080/v3/api-docs 2. http://localhost:8080/swagger-ui/index.html
4. AOP на cache - выбор по @Profile.
5. AOP на логирование.
6. exception-starter.
7. Spring Security.
8. Spring Cloud Config
9. JavaDoc.

#### Запуск: через docker-compose.yml 
Зайдите в корень проекта, где находится файл docker-compose.yml,
в ней же откройте командную строку и введите: docker network create app-network && docker compose up - для загрузки
контейнера postgres,(-d) - в фоновом режиме.

#### Описание контроллеров


- Аутентификация
  ```sh
   POST /api/v1/auth/authenticate
   ```
- Регистрация: по умолчанию ROLE_SUBSCRIBER

   ```sh
      POST /api/v1/auth/register
   ```

   
- Авторизация с помощью jwt-токена:

Доступные адреса:

- работа с новостями
> News:
id,
title,
time,
textNews,
author
comments
- Создавние новости возможно только журналисту и администратору
```sh
POST /news
```
- Поиск по id
```sh
GET /news/{id}
```
- Список с учетом пагинации
```sh
GET /news?pageNumber={pageNumber}&pageSize={pageSize}
```
- Выбор заданного News, по его id и его комментариев.
```sh
GET /news/{newsId}/comments
```
- Выбор заданного News, по его id и комментария этого News по id.
```sh
GET /news/{newsId}/comments/{commentsId}
```
- Обновляются только те поля, которые передаются в запросе (доступно журналисту, только свои новости и администратору)
```sh
PUT /news/{id}
```
- Удаление по id (доступно журналисту только свои новости и администратору)
```sh
DELETE /news/{id}
```
- Полнотекстовый поиск: Запросы с подстановочными знаками
```sh
GET /news/titles/{str}/predicate?pageNumber={pageNumber}&pageSize={pageSize}
```
- Полнотекстовый поиск: Фразовые запросы
```sh
GET /news/texts/{str}/phrase?pageNumber={pageNumber}&pageSize={pageSize}
```
- Работа с коментариями
>Comment:
id,
username,
time,
text,
textComment
news_id
- Создание коментария возможно только подписчику и администратору
 ```sh
POST /comments
```
- Поиск по id
```sh
GET /comments/{id}
```
- Список с учетом пагинации
```sh
GET /comments?pageNumber={pageNumber}&pageSize={pageSize}
```
- Обновляются только те поля, которые передаются в запросе (доступно подписчику, только свои коментарии и администратору.)
```sh
PUT /comments/{id}
```
- Удаление по id (доступно подписчику, только свои коментарии и администратору.)
```sh
DELETE /comments/{id}
```  
- Полнотекстовый поиск: Запросы с подстановочными знаками
```sh
GET /omments/titles/{str}/predicate?pageNumber={pageNumber}&pageSize={pageSize}
```
- Полнотекстовый поиск: Фразовые запросы
```sh
GET /comments/texts/{str}/phrase?pageNumber={pageNumber}&pageSize={pageSize}
```