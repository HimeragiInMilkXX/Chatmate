# EasyProfile Auth Starter

Production-ready reusable authentication + dynamic profile base library for Spring Boot 3.x.

## Modules

- `easyprofile-auth-spring-boot-starter`: Reusable starter/library module.
- `easyprofile-demo-app`: Example application that consumes the starter.

## Why EAV for dynamic fields

This library **does not ALTER TABLE users at runtime**. Instead it uses:

- `user_profile_fields`: logical field definitions (`addColumn`) with type metadata.
- `user_profile_values`: typed per-user values.

This is safer and easier to maintain than runtime schema mutation.

## Features

- Java 17+, Spring Boot 3.x
- Sa-Token authentication/session/token
- BCrypt password hashing
- MySQL-first + Flyway migrations
- Multipart avatar upload with pluggable storage
- Dynamic user profile fields (`addColumn`, `get`, update support)
- Global JSON response wrapper
- Global exception handling
- Password never exposed in API responses

## Public Java API

`DynamicProfileService`

- `addColumn(String columnName, DataType dataType)`
- `addColumn(String columnName, DataType dataType, ReferenceOptions options)`
- `Map<String,Object> get(String... columns)` for current user
- `Map<String,Object> getForUser(Long userId, String... columns)`
- `void setForCurrentUser(Map<String,Object> updates)`

`DataType`:

- `STRING`, `INT`, `LONG`, `DOUBLE`, `BOOLEAN`, `DATE`, `DATETIME`, `TEXT`, `REFERENCE`

## Endpoints

Base: `/auth`

- `POST /register`
- `POST /login`
- `GET /me`
- `POST /avatar` (`multipart/form-data`, `file`)
- `PATCH /update`
- `POST /resetPassword`
- `POST /logout`

Demo app endpoint:

- `GET /user/get/{id}`
- `POST /notification/create`
- `GET /notification/user/{userId}`
- `PATCH /notification/confirm/{id}`

All responses use:

```json
{
  "success": true,
  "message": "ok",
  "data": {}
}
```

## Configuration

```yaml
authlib:
  enabled: true
  cors:
    enabled: true
    allowed-origins:
      - http://localhost:3000
    allowed-methods:
      - GET
      - POST
      - PATCH
      - PUT
      - DELETE
      - OPTIONS
    allowed-headers:
      - "*"
    allow-credentials: true
    max-age: 3600
  avatar:
    upload-dir: ./uploads/avatars
    base-url: /uploads/avatars
    max-size-bytes: 5242880
  reset-password:
    cooldown-days: 7
  token-timeout: 604800
  security:
    require-old-password-on-reset: true
```

Sa-Token example:

```yaml
sa-token:
  token-name: satoken
  is-read-header: true
  is-read-cookie: false
  is-log: false
```

## Integrating in any host project

1. Add dependency:

```xml
<dependency>
  <groupId>com.easyprofile</groupId>
  <artifactId>easyprofile-auth-spring-boot-starter</artifactId>
  <version>1.0.0</version>
</dependency>
```

2. Configure datasource + Flyway + `authlib` properties.
3. Start app, Flyway creates required tables.
4. Use `/auth/*` endpoints immediately.

## CORS (frontend on localhost:3000)

If your frontend runs on `http://localhost:3000`, configure allowed origins so browser preflight (`OPTIONS`) succeeds:

```yaml
authlib:
  cors:
    allowed-origins:
      - http://localhost:3000
```

For production, use explicit trusted origins. Avoid wildcard origins.

## Where to call `addColumn` in demo app

In this demo, register dynamic fields at startup using a `CommandLineRunner`.  
This keeps schema-like field registration out of request flow and makes boot idempotent.

`easyprofile-demo-app/src/main/java/com/easyprofile/demo/DemoProfileFieldInitializer.java`:

```java
@Configuration
public class DemoProfileFieldInitializer {

    @Bean
    public CommandLineRunner registerDemoProfileFields(DynamicProfileService dynamicProfileService) {
        return args -> {
            registerField(dynamicProfileService, "age", DataType.INT);
            registerField(dynamicProfileService, "bio", DataType.TEXT);
            registerField(dynamicProfileService, "birthday", DataType.DATE);
            registerReferenceField(dynamicProfileService, "favoriteForumId", ReferenceOptions.of("forum", "id", false));
        };
    }

    private void registerField(DynamicProfileService dynamicProfileService, String fieldName, DataType dataType) {
        try {
            dynamicProfileService.addColumn(fieldName, dataType);
        } catch (ResourceConflictException ex) {
            // field already exists, ignore
        }
    }
}
```

## Dynamic field usage example

```java
@Autowired
private DynamicProfileService dynamicProfileService;

public void init() {
    dynamicProfileService.addColumn("age", DataType.INT);
    dynamicProfileService.addColumn("bio", DataType.TEXT);
    dynamicProfileService.addColumn("favoriteForumId", DataType.REFERENCE, ReferenceOptions.of("forum", "id", false));
}
```

`PATCH /auth/update` supports top-level dynamic fields once registered:

```json
{ "username": "tom", "age": 28, "bio": "hi" }
```

`REFERENCE` fields are validated through `ReferenceLookupService`.  
Provide your own bean in the host project to check whether target rows exist:

```java
@Bean
public ReferenceLookupService referenceLookupService() {
    return (target, key, value) -> {
        if ("forum".equalsIgnoreCase(target)) {
            Long id = value instanceof Number n ? n.longValue() : Long.parseLong(String.valueOf(value));
            return forumRepository.existsById(id);
        }
        return false;
    };
}
```

## Reset password policy

By default, `oldPassword` is required and reset is limited to once every 7 days.

- `authlib.security.require-old-password-on-reset=true|false`
- `authlib.reset-password.cooldown-days=7`

## Avatar storage extensibility

The default storage implementation writes locally. Override by providing your own `AvatarStorageService` bean (OSS/S3/etc).

## Migrations

Flyway migration is in:

- `easyprofile-auth-spring-boot-starter/src/main/resources/db/migration/V1__init_authlib_tables.sql`
- `easyprofile-auth-spring-boot-starter/src/main/resources/db/migration/V2__add_reference_metadata_for_dynamic_fields.sql`
- `easyprofile-demo-app/src/main/resources/db/migration/V3__create_notifications_table.sql`

## Tests included

Integration tests cover:

- register
- login
- me
- update
- reset password cooldown
- dynamic field add/get behavior
- avatar upload

## Running demo app

`easyprofile-demo-app` includes sample `application.yml`. Update MySQL credentials and run the application.

## Notes

- Password is never returned by API DTOs.
- Reserved/system field names are blocked for dynamic registration.
- This repository does not include Maven Wrapper by default; install Maven locally to build.
