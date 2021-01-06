# Выпускной проект Topjava
## Задача

Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

The task is:

Build a voting system for deciding where to have lunch.

2 types of users: admin and regular users
Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
Menu changes each day (admins do the updates)
Users can vote on which restaurant they want to have lunch at
Only one vote counted per user
If user votes again the same day:
If it is before 11:00 we asume that he changed his mind.
If it is after 11:00 then it is too late, vote can't be changed
Each restaurant provides new menu each day.

As a result, provide a link to github repository.

It should contain the code and README.md with API documentation and curl commands to get data for voting and vote.

P.S.: Make sure everything works with latest version that is on github :)

P.P.S.: Asume that your API will be used by a frontend developer to build frontend on top of that.

## Документация
### Описание API
#### Сущности
* Пользователь (**User**): id, name, email, password, enabled, registered, roles; **UserTo**: id, name, email, password
* Ресторан (**Restaurant**): id, name; dishes
* Блюдо ресторана (**Dish**): id, name, date, price; **DishTo**: id, name, price

#### Формат описания запроса
<Тип_запроса> <URI> [Параметры_запроса] [(Тело_запроса)] [: <Данные_ответа>]

Для всех запросов, кроме регистрации необходима авторизация. Поддерживается базовая авторизация: email, password

#### Для пользователей
* Регистрация: Post /api/register (UserTo): User
* Данные о пользователе: Get /api/profile : UserTo
* Изменить данные: Put /api/profile (UserTo)
* Удалить данные: Delete /api/profile
* Список ресторанов: Get /api/restaurants ?withDishes (default=true) : список Restaurant
    - Результат запроса кешируется на 12 часов
* Данные о ресторане: Get /api/restaurants/{id} ?withDishes (default=true): Restaurant
* Все блюда ресторана: Get /api/restaurant/{id}/dishes: список Dish
* Проголосовать: Post /api/restaurant/{id}/vote

#### Для администратора
##### Работа с пользователями
* Список: GET /api/admin/users: список User
* Данные об одном: GET /api/admin/users/{id}: User
* Создать: POST /api/admin/users (UserTo): User
* Изменить: PUT /api/admin/users/{id} (UserTo)
* Удалить: DELETE /api/admin/users/{id}

##### Работа с ресторанами
* Список: GET /api/admin/restaurants: список Restaurant
    - Результат запроса кешируется
* Данные об одном: GET /api/admin/restaurants/{id}: Restaurant
* Создать: POST /api/admin/restaurants (Restaurant): Restaurant
* Изменить: PUT /api/admin/restaurants/{id} (Restaurant)
* Удалить: DELETE /api/admin/restaurants/{id}

##### Работа с меню ресторана
* Список: GET /api/admin/restaurants/{restaurantId}/dishes: список Dish
* Данные об одном блюде: GET /api/admin/restaurants/{restaurantId}/dishes/{id}: Dish
* Создать: POST /api/admin/restaurants/restaurantId/dishes (DishTo): Dish
* Изменить: PUT /api/admin/restaurants/{restaurantId}/dishes/{id} (DishTo)
* Удалить: DELETE /api/admin/restaurants/{restaurantId}/dishes/{id}

### Примеры команд (для приложения, развернутого в контексте "/")
#### User registration
`curl -s -X POST -d '{"name" : "Registered User", "email" : "registered@world.org", "password" : "reguser"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/register`
#### User update profile
`curl -s -X PUT -d '{"id":100000,"name":"Updated User","email":"user@world.org","password":"user"}' -H 'Content-Type:application/json' http://localhost:8080/api/profile --user user@world.org:user`
#### User get restaurants with dishes
`curl -s http://localhost:8080/api/restaurants --user user@world.org:user`
#### User vote
`curl -s -X POST http://localhost:8080/api/restaurants/100002/vote --user user@world.org:user`
#### Admin get users
`curl -s http://localhost:8080/api/admin/users --user admin@world.org:admin`
#### Admin get user
`curl -s http://localhost:8080/api/admin/users/100000 --user admin@world.org:admin`
#### Admin create user
`curl -s -X POST -d '{"name" : "New User", "email" : "newuser@world.org", "password" : "newuser", "roles" : ["USER"]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/admin/users --user admin@world.org:admin`
#### Admin update user
`curl -s -X PUT -d '{"id":100000,"name":"Updated User","email":"user@world.org","password":"user","registered":"2017-05-20T05:06:09.711+0000","enabled":false,"roles":["USER"]}' -H 'Content-Type:application/json' http://localhost:8080/api/admin/users --user admin@world.org:admin`
#### Admin delete user
`curl -s -X DELETE http://localhost:8080/api/admin/users/100011 --user admin@world.org:admin`
#### Admin get restaurants
`curl -s http://localhost:8080/api/admin/restaurants --user admin@world.org:admin`
#### Admin get restaurant
`curl -s http://localhost:8080/api/admin/restaurants/100002 --user admin@world.org:admin`
#### Admin create restaurant
`curl -s -X POST -d '{"name" : "Friends and Family"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/admin/restaurants --user admin@world.org:admin`
#### Admin update restaurant
`curl -s -X PUT -d '{"id" : "100002", "name" : "Не прага"}' -H 'Content-Type:application/json' http://localhost:8080/api/admin/restaurants/100002 --user admin@world.org:admin`
#### Admin delete restaurant
`curl -s -X DELETE http://localhost:8080/api/admin/restaurants/100002 --user admin@world.org:admin`
#### Admin get restuarant dishes
`curl -s http://localhost:8080/api/admin/restaurants/100002/dishes --user admin@world.org:admin`
#### Admin get dish
`curl -s http://localhost:8080/api/admin/restaurants/100002/dishes/100004 --user admin@world.org:admin`
#### Admin create dish
`curl -s -X POST -d '{"name" : "Toast", "price" : 99}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/api/admin/restaurants/100002/dishes --user admin@world.org:admin`
#### Admin update dish
`curl -s -X PUT -d '{"id" : "100004", "name" : "Dorado", "price" : "199"}' -H 'Content-Type:application/json' http://localhost:8080/api/admin/restaurants/100002/dishes/100004 --user admin@world.org:admin`
#### Admin delete dish
`curl -s -X DELETE http://localhost:8080/api/admin/restaurants/100002/dishes/100004 --user admin@world.org:admin`

## Используемые инструменты и технологии
* Maven
* Java 11
* Spring (Data JPA, MVC, Security, Test, Security test)
* Hibernate
* SLF4J, Logback
* HSQLDB
* JUnit
* Json (Jackson)
* EhCache
* jsoup
