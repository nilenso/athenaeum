# athenaeum

Track your books

## setup

#### frontend setup

- To run the development build with hot-reloading, install [yarn](https://classic.yarnpkg.com/en/docs/install) and run:

    ```
    $ yarn install
    $ yarn start
    ```

- To generate the release build, run:

    ```
    $ yarn release
    ```

#### backend setup

- To start up the backend server from the terminal, install [Leiningen](https://leiningen.org/#install) and run:

    ```
    $ lein deps
    $ lein run
    ```
    or from the REPL:

    ```
    (athenaeum.config/load-config)
    (athenaeum.server/start-app)
    ```

    Then navigate to http://localhost:8080/.

- To run tests:

    ```
    $ lein test
    ```

#### linting & formatting

- Install [clj-kondo](https://github.com/borkdude/clj-kondo) and run linter for source and test files:

    ```
    clj-kondo --lint src/
    clj-kondo --lint test/
    ```

- Fix formatting of source code:

    ```
    lein cljfmt fix
    ```

 Run these beforehand to ensure CI doesn't fail.
