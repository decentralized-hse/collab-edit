# collab-edit

## Building client

```shell
./gradlew :client:browserProductionWebpack
```

After it, you can open `client/build/distributions/index.html` in a browser.

## Running singnaling server

```shell
./gradlew :server-signal:run
```

## Running integration tests

```shell
./gradlew -Dwebdriver.chrome.driver=/path/to/chromedriver :server-signal:integrationTest
```
