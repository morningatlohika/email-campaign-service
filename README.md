# ecs
Email campaign service

## Task tracking
We use [Trello card](https://trello.com/c/n79QeVre/40-%D0%B0%D0%B2%D1%82%D0%BE%D0%BC%D0%B0%D1%82%D0%B8%D0%B7%D1%83%D0%B2%D0%B0%D1%82%D0%B8-%D1%80%D0%BE%D0%B7%D1%81%D0%B8%D0%BB%D0%BA%D1%83-%D0%B5%D0%BC%D0%B5%D0%B9%D0%BB%D1%96%D0%B2)
to track tasks. Issues should be reported in the [github repo](https://github.com/morningatlohika/ecs)

## Database
We use embedded DynamoDB to store events, talks and speakers. The data is stored on filesystem.
The location is described in the [Configuration/DB location](#db-location) section.
If the database doesn't exist at the specified location, it will be automatically created during app startup.

## Configuration
### Default properties
Default location for any application properties, including secrets, is `$HOME/.morning/ecs/` directory.
This location can be customized by specifying `--spring.config.location=<your_location>` in the application command line.
Eventual properties location is logged early during the application startup.

### DB location
Default DB location is `${spring.config.location}/data` and may be customized by specifying the `--derby.system.home=<custom_db_location>` system property.
Eventual DB location is logged early during the application startup.

### Spring profiles
Default spring profile is `local`. It should be used for development.
For production, `prod` profile should be used.


