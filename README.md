# athenaeum

Track your books

### Setup

#### frontend

- To run the development build with hot-reloading, install [yarn](https://classic.yarnpkg.com/en/docs/install) and run:

    ```shell script
    yarn install # only needs to be run once
    yarn start
    ```

- To generate the release build:

    ```shell script
    yarn release
    ```

#### backend

- Install [Redis](https://redis.io/download) (reccommended: v6.0.9) and run its server.

- Install [Postgres](https://www.postgresql.org/download/) (reccommended: v13.1) and create dev and test databses with specs mentioned in their respective [config files](https://github.com/nilenso/athenaeum/tree/master/config).

- To start the backend server from the REPL, run:

    ```clojure
    (dev.repl-utils/start-app)
    ```

    or from the terminal, install [Leiningen](https://leiningen.org/#install) and run:

    ```shell script
    lein deps
    lein run config/config.dev.edn
    ```

    Then navigate to http://localhost:8080/.

### Migrations

- You can run these from the REPL with:

  ```clojure
  (athenaeum.migrations/migrate)
  ```

  or from the terminal with:

  ```shell
  lein run config/config.dev.edn migrate # pass appropriate config files for dev and test migrations
  ```


### Testing

- To run frontend tests, install [node](https://nodejs.org/en/download/) and run:

    ```shell script
    yarn test
    ```

- To run server tests, first run test migrations:

    ```shell script
    lein run config/config.test.edn migrate # whenever migrations are changed/added
    lein test
    ```

### Linting & formatting

- Install [clj-kondo](https://github.com/borkdude/clj-kondo) and run linter on source and test files:

    ```shell script
    clj-kondo --lint src/ test/
    ```

- Fix formatting of source code with:

    ```shell script
    lein cljfmt fix
    ```

### CI/CD

Ensure linting and formatting, along with frontend and backend tests are passing so that all checks pass.
