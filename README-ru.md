# Graduation work in on-line project <a href="https://github.com/JavaOPs/topjava">Topjava</a>
## Formulation of the problem
Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and couple curl commands to test it.

## Documentation
### API
#### Entities
* **User**: id, name, email, password, enabled, registered, roles; **UserTo**: id, name, email, password
* **Restaurant**: id, name; dishes
* **Dish**: id, name, date, price; **DishTo**: id, name, price

#### Request format description
<HTTP_Type> <URI> [Params] [(Body)] [: <Response_Type>]

For all requests authorization is required. Basic Authorization (email + password) is supported.

#### For users
* Registration: Post /api/register (UserTo): User
* User profile: Get /api/profile : UserTo
* Change profile: Put /api/profile (UserTo)
* Delete profile: Delete /api/profile
* List of restaurants: Get /api/restaurants ?withDishes (default=true) : список Restaurant
    - Note: request result caches for 12 hours
* Info about restaurant: Get /api/restaurants/id ?withDishes (default=true): Restaurant
* Menu for restaurant: Get /api/restaurant/id/dishes: список Dish
* Vote: Post /api/restaurant/id/vote

#### For Admins
##### Work with users
* List: Get /api/admin/users: список User
* Get single user: Get /api/admin/users/id: User
* Create: Post /api/admin/users (UserTo): User
* Update: Put /api/admin/users/id (UserTo)
* Delete: Delete /api/admin/users/id

##### Work with restaurants
* List: Get /api/admin/restaurants: список Restaurant
    - Note: request result caches
* Get single restaurant: Get /api/admin/restaurants/id: Restaurant
* Create: Post /api/admin/restaurants (Restaurant): Restaurant
* Update: Put /api/admin/restaurants/id (Restaurant)
* Delete: Delete /api/admin/restaurants/id

##### Work with restaurant menu
* List: Get /api/admin/restaurants/restaurantId/dishes: список Dish
* Get single dish: Get /api/admin/restaurants/restaurantId/dishes/id: Dish
* Create: Post /api/admin/restaurants/restaurantId/dishes (DishTo): Dish
* Update: Put /api/admin/restaurants/restaurantId/dishes/id (DishTo)
* Delete: Delete /api/admin/restaurants/restaurantId/dishes/id

### Request samples (application deployed in application context topjava_graduation)
#### User registration
`curl -s -X POST -d '{"name" : "Registered User", "email" : "registered@world.org", "password" : "reguser"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/topjava_graduation/api/register`
#### User profile
`curl -s http://localhost:8080/topjava_graduation/api/profile --user user@world.org:user`
#### User update profile
`curl -s -X PUT -d '{"id":100000,"name":"Updated User","email":"user@world.org","password":"user"}' -H 'Content-Type:application/json' http://localhost:8080/topjava_graduation/api/ --user user@world.org:user`
#### User get restaurants with dishes
`curl -s http://localhost:8080/topjava_graduation/api/restaurants --user user@world.org:user`
#### User vote
`curl -s -X POST http://localhost:8080/topjava_graduation/api/restaurants/100002/vote --user user@world.org:user`
#### Admin get users
`curl -s http://localhost:8080/topjava_graduation/api/admin/users --user admin@world.org:admin`
#### Admin get user
`curl -s http://localhost:8080/topjava_graduation/api/admin/users/100000 --user admin@world.org:admin`
#### Admin create user
`curl -s -X POST -d '{"name" : "New User", "email" : "newuser@world.org", "password" : "newuser", "roles" : ["USER"]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/topjava_graduation/api/ --user admin@world.org:admin`
#### Admin update user
`curl -s -X PUT -d '{"id":100000,"name":"Updated User","email":"user@world.org","password":"user","registered":"2017-05-20T05:06:09.711+0000","enabled":false,"roles":["USER"]}' -H 'Content-Type:application/json' http://localhost:8080/topjava_graduation/api/ --user admin@world.org:admin`
#### Admin delete user
`curl -s -X DELETE http://localhost:8080/topjava_graduation/api/ --user admin@world.org:admin`
#### Admin get restaurants
`curl -s http://localhost:8080/topjava_graduation/api/admin/restaurants --user admin@world.org:admin`
#### Admin get restaurant
`curl -s http://localhost:8080/topjava_graduation/api/admin/restaurants/100002 --user admin@world.org:admin`
#### Admin create restaurant
`curl -s -X POST -d '{"name" : "Friends and Family"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/topjava_graduation/api/admin/restaurants --user admin@world.org:admin`
#### Admin update restaurant
`curl -s -X PUT -d '{"id" : "100002", "name" : "Не прага"}' -H 'Content-Type:application/json' http://localhost:8080/topjava_graduation/api/admin/restaurants/100002 --user admin@world.org:admin`
#### Admin delete restaurant
`curl -s -X DELETE http://localhost:8080/topjava_graduation/api/admin/restaurants/100002 --user admin@world.org:admin`
#### Admin get restuarant dishes
`curl -s http://localhost:8080/topjava_graduation/api/admin/restaurants/100002/dishes --user admin@world.org:admin`
#### Admin get dish
`curl -s http://localhost:8080/topjava_graduation/api/admin/restaurants/100002/dishes/100004 --user admin@world.org:admin`
#### Admin create dish
`curl -s -X POST -d '{"name" : "Toast", "price" : 99}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/topjava_graduation/api/admin/restaurants/100002/dishes --user admin@world.org:admin`
#### Admin update dish
`curl -s -X PUT -d '{"id" : "100004", "name" : "Dorado", "price" : "199"}' -H 'Content-Type:application/json' http://localhost:8080/topjava_graduation/api/admin/restaurants/100002/dishes/100004 --user admin@world.org:admin`
#### Admin delete dish
`curl -s -X DELETE http://localhost:8080/topjava_graduation/api/admin/restaurants/100002/dishes/100004 --user admin@world.org:admin`

## Used tools and frameworks
* Maven
* Java 8
* Spring (Data JPA, MVC, Security, Test, Security test)
* Hibernate
* SLF4J, Logback
* HSQLDB
* JUnit
* Json (Jackson)
* EhCache
* jsoup
