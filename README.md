# ecs
Email campaign service

## Task tracking
We use [Trello card](https://trello.com/c/n79QeVre/40-%D0%B0%D0%B2%D1%82%D0%BE%D0%BC%D0%B0%D1%82%D0%B8%D0%B7%D1%83%D0%B2%D0%B0%D1%82%D0%B8-%D1%80%D0%BE%D0%B7%D1%81%D0%B8%D0%BB%D0%BA%D1%83-%D0%B5%D0%BC%D0%B5%D0%B9%D0%BB%D1%96%D0%B2)
to track tasks. Issues should be reported in the [github repo](https://github.com/morningatlohika/ecs)

## Configuration
### Default properties
Default location for any properties, including secrets, is `$HOME/.morning/ecs/` directory.
This location can be customized by specifying `--spring.config.location=<your_location>` in the application command line.

### Spring profiles
Default spring profile is `local`. It should be used for development.
For production, `prod` profile should be used.
