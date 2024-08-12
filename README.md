## ExploreWithMe.

Веб приложение для создания и участия в мероприятиях.  
Приложение позволяет создавать собственные мероприятия, управлять ими, участвовать в чужих, комментировать прошедшие события.  

Реализация состит из двух модулей, каждый запускается в своем контейнере:
1. Основной сервис
- Публичная часть позволяет возможность поиска и фильтрации событий.
- Приватная часть позволяет создавать мероприятия, принимать заявки на участие в своих и подавать заявки на участие в чужих.
- Административная часть включает в себя управление пользователями, категориями событий, модерирование событий и комментариев к ним, создание подборок событий.
2. Сервис статистики
- Сервис статистики собирает информацию о просмотрах событий

Данные хранятся в PostgreSQL, для кажного модуля запускается свои контейнер с БД.

Для запуска приложения необходимо запустить docker-compose.yml
  
[Swagger спецификация основного сервиса](https://github.com/MaxxMurygin/java-explore-with-me/blob/main/ewm-main-service-spec.json)  
[Swagger спецификация сервиса статистики](https://github.com/MaxxMurygin/java-explore-with-me/blob/main/ewm-stats-service-spec.json)

[Postman тесты основного сервиса](https://github.com/MaxxMurygin/java-explore-with-me/blob/main/postman/ewm-main-service.json)  
[Postman тесты сервиса статистики](https://github.com/MaxxMurygin/java-explore-with-me/blob/main/postman/ewm-stat-service.json)  
[Postman тесты фичи комментариев и их модерирования](https://github.com/MaxxMurygin/java-explore-with-me/blob/main/postman/feature.json)  
