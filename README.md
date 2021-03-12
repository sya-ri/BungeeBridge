# BungeeBridge
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/sya-ri/BungeeBridge)](https://github.com/sya-ri/BungeeBridge/releases/latest)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)

人数管理用のサーバー(WebAPI) を用意することで 複数の Bungeecord サーバー同士で人数情報を共有する。

※ プレイヤーを利用する Bungeecord のプラグインは正常に動作しません。v2.0.0 で WebAPI から WebSocket に変更し、PluginMessage のような機能を実装する予定です。

## 設定

### 人数管理用のサーバー `server`

1. `BungeeBridge-server-1.0.0.jar` をダウンロードする。

2. 設定ファイルを生成するために起動する。
```
java -jar BungeeBridge-server-1.0.0.jar
```

3. `option.properties` の設定をする。

| キー | 値 | デフォルト |
|-----|-------|---------|
| Port | ポート番号 | 8080 |
| CrashTime | クラッシュ扱いにする時間(ms) | 30000(30s) |

```properties
Port=8080
CrashTime=30000
```

- `Port` を変更した場合、`plugin` 側の設定を変える必要がある。また、ローカル以外におく場合、ポート開放をする必要がある。
- Bungeecord がクラッシュし、プレイヤーの更新リクエストが途絶えてから `CrashTime` の時間が経過した場合、自動で人数の削除を行う。

### Bungeecord プラグイン `plugin`

1. `BungeeBridge-plugin-1.0.0.jar` をダウンロードし、Bungeecord の plugins に入れる。

2. 設定ファイルを生成するために起動する。

3. `plugins/BungeeBridge/config.yml` の設定をする。

| パス | 値 | デフォルト |
|-----|-------|---------|
| url | API サーバーの URL | http://localhost:8080 |
| serverName | Bungeecord の識別子。被らなければ好きにつけてよい。 | randomUUID |
| updateTime | 人数更新の間隔(ms) | 1000(1s) |

```yml
url: http://localhost:8080
serverName: ""
updateTime: 1000
```

4. 再起動する。`Fail Connection` エラーが出ずに `server` 側にログが流れていれば接続できている。

## コマンド

| コマンド | 別名 |説明 |
|--------|------|------|
| /blist | | Bungeecord 単位でプレイヤー一覧を表示する。|
| /glist | /gblist | 全ての Bungeecord サーバーのプレイヤー一覧を表示する。 |
| /glist:bungee | /bungee:glist | 本来の /glist コマンドを実行する。同じ Bungeecord にいるプレイヤーのみが表示される。 |

## 開発者向け

### WebAPI

#### POST `/update`
プレイヤー情報の更新を行う。最後の更新から `option.properties > CrashTime` の時間が経過するとクラッシュしたとみなされ、プレイヤー情報が削除されてしまうので、変動がなかったとしても定期的に実行する必要がある。

##### Request

| フィールド | 種類 | 説明 |
|----------|------|-----|
| name | String | Bungeecordのサーバー名(`config.yml > serverName`) |
| players | Map<String, String> | プレイヤー名とSpigotのサーバー名のオブジェクト |

###### 例
```json
{
  "name": "Bungee1",
  "players": {
    "sya_ri": "SpigotA"
  }
}
```

##### Response

| フィールド | 種類 | 説明 |
|----------|------|-----|
| allCount | Int | 全ての Bungeecord のプレイヤー人数 |
| all | Boolean | true だった時、Bungeecord の全てのプレイヤー情報を再送する必要がある。初めてのリクエストだったかを表す。 |

###### 例
```json
{
  "allCount": 5,
  "all": false
}
```

#### POST `/clear`
プレイヤー情報の削除を行う。

| フィールド | 種類 | 説明 |
|----------|------|-----|
| name | String | Bungeecordのサーバー名(`config.yml > serverName`) |

###### 例
```json
{
  "name": "Bungee1"
}
```

#### GET `/list`
全ての Bungeecord サーバーのプレイヤー一覧を取得する。

##### Response

| フィールド | 種類 | 説明 |
|----------|------|-----|
| | Map<String, ServerData> | サーバーとサーバーの情報のオブジェクト |

> `ServerData`
>
> | フィールド | 種類 | 説明 |
> |----------|------|-----|
> | players | Map<String, String> | プレイヤー名とSpigotのサーバー名のオブジェクト |

###### 例
```json
{
  "Bungee1": {
    "sya_ri": "SpigotA",
    "anotherPlayerA": "SpigotB"
  },
  "Bungee2": {
    "anotherPlayerB": "SpigotA",
    "anotherPlayerC": "SpigotA"
  }
}
```

### Gradle

#### プロジェクト

| プロジェクト名 | 説明 |
|--------------|-----|
| plugin | Bungeecord プラグインのプロジェクト |
| server | WebAPI サーバーのプロジェクト |
| shared | 共通部分のライブラリ |

#### ビルド

```shell
gradle shadowJar
```

以上のビルドタスクの実行すると、`plugin/build/libs`, `server/build/libs` にそれぞれ生成される。
