# collab-edit

Edit text in collaboration! This is a simple text editor with support of multiple connected users working on the same text, available as a web application ready to be hosted on premises. It features CRDT algorithms to properly merge changes in the text.

## Contents

- [Quick start](#quick-start).
- [How it works](#how-it-works).
- [Current limitations](#current-limitations).
- [Developers zone](#developers-zone).

## Quick start

The following command will run the server:

```shell
./gradlew :server-signal:run      # for Linux/Mac
.\gradlew.bat :server-signal:run  # for Windows
```

After it, you can open <localhost:9090> in a browser.

The client will connect to the signaling server. After it, you can enter your name and the name of your friend you want to collaborate with (obviously, your friend should be connected to the same signaling server too). In the end, you and your friend will get a text area that you can edit simultaneously.

## How it works

Currently, the state of text and cursor positions are stored as [Chronofold](https://arxiv.org/abs/2002.09511) on each client. When the user updates the text or the cursor position on one client, it calculates diff via [diff-match-patch](https://github.com/google/diff-match-patch), updates its Chronofold, and sends new operations to another client. Another client adds these operations to its Chronofold and updates the text and cursor positions accordingly.

Since Chronofold is a Conflict-free Replicated Data Type (CRDT), this merging should work even if the ping is high and simultaneous edits are happening on the both sides.

### Connection through signal server

This way the signaling server behaves like a relay:

![websocket](https://kroki.io/c4plantuml/svg/eNqVUUFqwzAQvPsVW58cMIGYPiDFHyhxoccg21N3qbwy0rpt-vquE0hCemkOAmlnZmdntU3qos6jzx5YOj_3oPpxXwdRfOt6WoCsOSTFWCTET8SS8oYHcZ5loFOJih28O6xyw3ZIU5DErQe9hUgRA5s6LuzZ6OJGpJK6IIJOOQixsLK1-3HHZwv9AoQ6zxA1qpP-mj46FrWzNLzh5qvsGTEFKU6F_cYGqo9X2vwBqwtYGZhZhnPEK_3TrO8lvdg2aJ56p0hLzFe0Teg-oKZchBfBeUv_E944Vvc6Vnc5biG9fekvncarsQ==)

### Peer-to-peer connection

This way the signaling server only allows to discover another client:

![webrtc](https://kroki.io/c4plantuml/svg/eNqFUctug0AMvPMVLiciVZWC-gGpuFcVROoRLTAlVhcv2jV9fX2XpEpoK5Sb7Xl4LO-CGq_TYJMbltZOHai4rwsnig-9G2cgqT6DYsgC_Bv8LaUV92IsS0-nEWUPkx42aYRKhNFJ4MaCXpwnj56j2M_kKbLFDAhkpKPWiaBVdkIsrBwNv8yxbaDvgFBrGaIh3SRP8MFJdhrU27inOJa0_QfmFzCPYFLCnoMv9HPgOe8zmsq1r9DInakXyvnaNeof1_y6a37dtX4E94fG-UWSxYJ9fApNY2cU4Udd7osVaf7r4lVpsoN08c_fxayucQ==)

## Current limitations

The most noticeable limitations are described below.

### WebRTC

For now, only Chromium-based browsers are supported when connecting through WebRTC. We have to use `webkit` API because universal API has problems with data channels: [1](https://github.com/webrtc/samples/issues/1227), [2](https://github.com/webrtc/samples/issues/1251). Also, even `webkit` API doesn't allow connecting sometimes.

It seems to support reliable connection though WebRTC, a full-fledged STUN/TURN server should be added to the app. Maybe this way universal WebRTC API will start working too.

### Without WebRTC

Connection though the signalling server obviously requires running it until the connection isn't needed anymore. On the contrary, WebRTC connection allows disconnecting from the signalling server when the connection between peers is established. 

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

### Possible future work

The project is already in the working state. However, there are many things yet to improve.

#### Optimizations

- Use more effective storage for Chronofold like using multiple arrays (one array for each field) instead of array of data classes.
- Implement custom text area to get rid of ineffective diff-match-patch invocation and iterating through casual tree on every input symbol to understand its position relative to other timestamps.

#### Code quality

- Move repeating parts of integration tests to functions or maybe even use another testing style.
- Add fuzzing or property tests for text representations translation in the client-side.
- Forbid entering `|` (caret symbol), add a test for it.
- For diff-match-patch: use more correct external declarations, for example, specify arrays.
- For diff-match-patch: create wrappers for pairs that are presented as JS arrays.
- Extract often-used React components as separate components.
- Add ID attributes to userName and otherUserName labels on the collaboration page, add integration tests for checking it.
- Move integration tests out of `server-signal` module to another module (this will be logical when we support work without signal server).
- Set up CI to build, test, and test integration of every commit.
- Set up CI to publish the client automatically.
- Set up CI to publish the server automatically.
- Add integration tests checking that even with high ping, everything merges correctly eventually.

#### Page design

- Extract header to somewhere more above, add link to this repo.
- Use our own design, get rid of react-bootstrap.

#### Features

- Support working without signal server (sending WebRTC offers and answers by the user, for example, via a messenger).
- Store EOF as a separate variable (don't remember why it's needed 🤔).
- Synchronize selection of text.
- Support rooms instead of current 1-to-1 connections.
