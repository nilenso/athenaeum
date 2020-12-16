# athenaeum

Track your books

## setup

#### frontend setup

- To run the development build with hot-reloading, install [yarn](https://classic.yarnpkg.com/en/docs/install) and run:

    ```shell script
    yarn install
    yarn start
    ```

- To generate the release build:

    ```shell script
    yarn release
    ```

- To run tests, install [node](https://nodejs.org/en/download/) and run:

    ```shell script
    yarn test
    ```

#### backend setup

- To start the backend server from the terminal, install [Leiningen](https://leiningen.org/#install) and run:

    ```shell script
    lein deps
    lein run
    ```
    or from the REPL:

    ```clojure
    (dev.repl-utils/start-app)
    ```

    Then navigate to http://localhost:8080/.

- To run tests:

    ```shell script
    lein test
    ```

#### linting & formatting

- Install [clj-kondo](https://github.com/borkdude/clj-kondo) and run linter for source and test files:

    ```shell script
    clj-kondo --lint src/ test/
    ```

- Fix formatting of source code:

    ```shell script
    lein cljfmt fix
    ```

 Run these beforehand to ensure CI doesn't fail.
