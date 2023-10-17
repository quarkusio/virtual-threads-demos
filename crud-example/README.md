# CRUD Demo

This demo shows how you can utilize virtual threads to implement a CRUD service.
It uses Hibernate ORM with Panache, Hibernate Bean Validation, the Narayana transaction manager and a PostgreSQL database.

**IMPORTANT:** You need Java 21 to build and run the demos.

**IMPORTANT:** You need a working docker or podman environment to run this demo.

## Build and run the demo

```shell
> just build # Build the application
> just dev # Run the application in dev mode
> just run # Build an run the application
> just build-native # Build a native image
> just run-native # Run the native image (don't forget to build it first)
```

When executed in _prod_ mode (with the `run` or `run-native` recipes), this application needs a database.
You can start a PostgresSQL database with docker or podman:

```shell
> just start-database
```

Stop the database with:

```shell
> just stop-database
```


Once running, the endpoint is: http://localhost:8080.

