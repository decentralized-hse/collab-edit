# collab-edit

Edit text in collaboration! This is a simple text editor with support of multiple connected users working on the same text, available as a web application ready to be hosted on premises. It features CRDT algorithms to properly merge changes in the text.

## Quick start

The following command will run the server:

```shell
./gradlew :server-signal:run      # for Linux/Mac
.\gradlew.bat :server-signal:run  # for Windows
```

After it, you can open <localhost:9090> in a Chromium-based browser.

The client will connect to the signaling server. After it, you can enter your name and the name of your friend you want to collaborate with. In the end, you and your friend will get a text area that you can edit simultaneously.

## Current limitations

The most noticeable limitations are described below.

### Supported browsers

For now, only Chromium-based browsers are supported. We have to use `webkit` API because universal API has problems with data channels: [1](https://github.com/webrtc/samples/issues/1227), [2](https://github.com/webrtc/samples/issues/1251).

### Simultaneous connections

Only two users per room are supported for now. In the future, it's possible to support rooms with multiple users that can edit the same text.

### Signaling server

Signaling server is the only way to let the users detect each other for now. We've planed this project as peer-to-peer, and now information about the edited text is sent between peers directly through WebRTC. In the future, it's possible to get rid of the need of a signaling server by introducing another way of detection, for example, when users have an arbitrary way of text communication to send bootstrap messages.

## Developers zone

### Building client

```shell
./gradlew :client:browserProductionWebpack
```

A set of static files ready to be served will be built in the `client/build/distributions/index.html` dir.

### Running signaling server

```shell
./gradlew -Dsignal.port=9090 :server-signal:run
```

It also serves the client, so you can open <localhost:9090> in a browser. If the port parameter is omitted, the default is 9090.

### Running integration tests

```shell
./gradlew -Dwebdriver.chrome.driver=/path/to/chromedriver :server-signal:integrationTest
```
