# collab-edit

## Supported browsers

For now, only Chromium-based browsers are supported. We have to use `webkit` API because universal API has problems with
data channels: [1](https://github.com/webrtc/samples/issues/1227), [2](https://github.com/webrtc/samples/issues/1251).

## Building client

```shell
./gradlew :client:browserProductionWebpack
```

After it, you can open `client/build/distributions/index.html` in a browser.

## Running signaling server

```shell
./gradlew :server-signal:run
```

## Running integration tests

```shell
./gradlew -Dwebdriver.chrome.driver=/path/to/chromedriver :server-signal:integrationTest
```
