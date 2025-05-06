---

## 📘 Posture Correction Project 要件定義書（JavaFXアプリ）

---

### 1. 📌 **プロジェクト概要**

**名称**：Posture Correction Project

**目的**：
ユーザーが日常の姿勢を「自分で評価・記録」し、可視化を通じて意識と改善行動を促すセルフケア支援アプリを提供する。

**開発手段**：

* Java 17 以降 + JavaFX によるデスクトップアプリ開発
* ローカル保存前提（ネットワーク連携なし）

**目指す成果物**：

* モダンでシンプルなUIを備えた自己管理ツール
* 毎日数回の簡単な入力、週単位・月単位での振り返りが可能
* 習慣化しやすいUX設計

---

### 2. 🔄 **現行業務フロー（手動で姿勢を意識する習慣）**

```
起床・仕事中 → 姿勢が悪くなる → 気づく・直す（偶発的）→ 特に記録せず忘れる → 翌日も繰り返す
```

**課題点**：

* 客観的な振り返りができない
* 継続的な改善行動が生まれにくい
* 意識が日常に埋もれ、姿勢改善が挫折しがち

---

### 3. 📋 **業務要件一覧**

| No. | 要件              | 説明                    |
| --- | --------------- | --------------------- |
| 1   | 姿勢評価を簡単に記録できること | 「良い・普通・悪い」などの選択式で記録可能 |
| 2   | メモを残せること        | 体調や気づきをテキストで入力可能      |
| 3   | 過去の記録を振り返られること  | カレンダーやグラフで推移を確認       |
| 4   | データはローカル保存されること | SQLiteまたはJSONによる保存    |
| 5   | 入力のしやすいUIを持つこと  | モダンで直感的な操作感           |
| 6   | 時間帯ごとの入力補助      | リマインダーや通知機能で習慣化を支援    |

---

### 4. 🔁 **システム化業務フロー**

```text
[ユーザー起動]
   ↓
[姿勢の自己評価入力]
   ↓
[オプションでコメント記入]
   ↓
[保存（自動orボタン）]
   ↓
[可視化画面（カレンダー/グラフ）で振り返り]
   ↓
[継続的な入力習慣により意識改善]
```

---

### 5. 🧩 **機能要件一覧**

| カテゴリ | 機能名      | 詳細                       |
| ---- | -------- | ------------------------ |
| 入力系  | 姿勢記録入力   | 「良い・普通・悪い」などのボタン選択式      |
| 入力系  | コメント記入   | テキストエリアで自由入力             |
| 表示系  | カレンダー表示  | 日々の姿勢評価を色付き表示（緑・黄・赤）     |
| 表示系  | グラフ表示    | 週/日単位の平均姿勢評価グラフ          |
| 設定系  | テーマ切替    | ダーク/ライトテーマ切り替え機能         |
| 通知系  | リマインダー   | 1日数回の入力通知（ポップアップ風UI）     |
| 保存系  | ローカル保存   | SQLiteまたはJSONで日付ごとの記録を保存 |
| その他  | 記録の編集・削除 | 入力済み記録を編集・削除可能に（任意）      |

---

### 6. 🛠️ **非機能要件一覧**

| 分類      | 要件      | 説明                                 |
| ------- | ------- | ---------------------------------- |
| パフォーマンス | 軽量起動・動作 | ローカル環境でストレスなく起動・反応                 |
| セキュリティ  | 外部送信なし  | インターネット通信は不要、個人情報は保存しない            |
| 保守性     | コード構造   | MVCベースで分離性を意識（JavaFX + FXML）       |
| 拡張性     | 機能追加に対応 | CSV出力・モバイル連携など将来の拡張が可能             |
| UI/UX   | モダンで直感的 | Figmaなどの参考デザインを取り入れたUI設計           |
| ポータビリティ | 複数OS対応  | Windows/macOS/LinuxでJava実行環境があれば動作 |
| データ持続性  | 長期保存    | 少なくとも1年以上のデータを保持して軽快に動作            |

---

## 📂 プロジェクト構成

```
posture-correction-project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── controller/
│   │   │   │   ├── MainController.java
│   │   │   │   ├── InputController.java
│   │   │   │   ├── CalendarController.java
│   │   │   │   ├── GraphController.java
│   │   │   │   └── SettingsController.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── PostureRecord.java
│   │   │   │   ├── RecordManager.java
│   │   │   │   └── StorageService.java
│   │   │   │
│   │   │   ├── view/
│   │   │   │   ├── main.fxml
│   │   │   │   ├── input.fxml
│   │   │   │   ├── calendar.fxml
│   │   │   │   ├── graph.fxml
│   │   │   │   └── settings.fxml
│   │   │   │
│   │   │   ├── util/
│   │   │   │   ├── DateUtil.java
│   │   │   │   └── ThemeManager.java
│   │   │   │
│   │   │   └── Main.java
│   │   │
│   │   └── resources/
│   │       ├── css/
│   │       │   ├── light-theme.css
│   │       │   └── dark-theme.css
│   │       ├── icons/
│   │       │   └── *.png
│   │       └── fonts/
│   │
│   └── test/
│       └── java/
│           └── (テストコード)
│
├── data/
│   └── records.json（または posture.db）
│
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
└── README.md
```

---

## 🖥️ **システム概要**

**名称**：Posture Correction Project

**概要**：
JavaFX を使って開発される、ユーザーが日々の姿勢を自己評価・記録し、可視化・振り返りを通じて姿勢改善を促進する自己管理アプリ。
記録はローカルに保存され、シンプルな操作性と視認性に優れたUIで、継続的な利用を支援します。

**システム構成**：

* クライアントアプリ（JavaFX Desktop App）
* ローカルDB（SQLite）またはJSONベースのストレージ

---

## 🗃️ **テーブル定義（SQLite想定）**

### ● posture_records テーブル

| カラム名           | データ型    | 制約          | 説明              |
| -------------- | ------- | ----------- | --------------- |
| id             | INTEGER | PRIMARY KEY | 自動採番ID          |
| record_date    | TEXT    | NOT NULL    | 記録日（YYYY-MM-DD） |
| time_slot      | TEXT    | NOT NULL    | 時間帯（朝・昼・夜）      |
| posture_level  | INTEGER | NOT NULL    | 姿勢評価（1:悪い〜3:良い） |
| comment        | TEXT    | NULLABLE    | コメント（任意）        |

---

## 🔗 **ER図（簡略版・1テーブル構成）**

```
┌────────────────────────────┐
│      posture_records       │
├──────────────┬────────────┤
│ id           │ INTEGER PK │
│ record_date  │ TEXT       │
│ time_slot    │ TEXT       │
│ posture_level│ INTEGER    │
│ comment      │ TEXT       │
└──────────────┴────────────┘
```

（単一ユーザー利用前提のため、他テーブルは不要）

---

## ✅ **使用ライブラリ**

| 分類        | 内容                                                                 |
| --------- | ------------------------------------------------------------------ |
| UIフレームワーク | JavaFX                                                             |
| DB        | SQLite（またはJSONファイル）                                                |
| グラフ描画     | [ChartsFX](https://github.com/GSI-CS-CO/chart-fx) や JavaFX 標準Chart |
| CSV出力     | OpenCSV, 独自実装も可能                                                   |
| スケジューラ    | JavaFXの `Timeline` や `ScheduledExecutorService`                    |
| ビルドツール    | Gradle                                                             |

---

## 🚀 **開発環境のセットアップ**

1. 必要なソフトウェア
   * Java 17 以降
   * Gradle
   * IDE（推奨：IntelliJ IDEA）

2. プロジェクトのクローン
   ```bash
   git clone [リポジトリURL]
   cd posture-correction-project
   ```

3. 依存関係のインストール
   ```bash
   ./gradlew build
   ```

4. アプリケーションの起動
   ```bash
   ./gradlew run
   ```

---

## 📝 **ライセンス**

このプロジェクトは MIT ライセンスの下で公開されています。

---

了解しました。以下に、**Posture Correction Project** における代表的な **結合テスト** と **システムテスト** を記述します。JavaFX + SQLite を想定しており、JUnit 5（＋ TestFX など）を使うことを前提としています。

---

## ✅ **1. 結合テスト（Integration Test）**

複数のモジュールや層（ViewModel ↔ Service ↔ DB）間の整合性と連携を確認します。

---

### 📄 テストケース：姿勢の保存 → DB登録確認

| テストID | IT-01                                                                                  |
| ----- | -------------------------------------------------------------------------------------- |
| 対象機能  | 姿勢記録保存（RecordManager 経由で DB に保存）                                                       |
| 目的    | RecordManager による記録保存が StorageService を経由して正しく DB に反映されるか                              |
| 前提条件  | SQLite DB が初期化済み                                                                       |
| 入力    | `recordDate = "2025-05-04"`, `timeSlot = "朝"`, `postureLevel = 2`, `comment = "普通だった"` |
| 期待結果  | DBの `posture_records` テーブルに該当データが存在する                                                  |

**JUnit コード例（簡略化）**：

```java
@Test
void testSavePostureRecord() {
    RecordManager manager = new RecordManager(new StorageService());
    PostureRecord record = new PostureRecord("2025-05-04", "朝", 2, "普通だった");

    manager.saveRecord(record);
    List<PostureRecord> records = manager.getRecordsByDate("2025-05-04");

    assertTrue(records.stream().anyMatch(r ->
        r.getTimeSlot().equals("朝") &&
        r.getPostureLevel() == 2 &&
        r.getComment().equals("普通だった")
    ));
}
```

---

### 📄 テストケース：統計取得の整合性（平均評価）

| テストID | IT-02                      |
| ----- | -------------------------- |
| 対象機能  | StatisticsService（評価平均の算出） |
| 前提条件  | 3件の記録（評価：1, 2, 3）を事前に挿入済み  |
| 期待結果  | 平均評価 = 2.0                 |

---

## ✅ **2. システムテスト（System Test）**

アプリ全体の流れに従い、ユーザー操作に対する反応とデータ整合性を確認するテストです。

TestFX または UI自動化ツールを使って GUI操作の検証が可能です（ここでは擬似的な手順と期待結果を記述）。

---

### 📄 テストケース：姿勢記録の一連フロー

| テストID | ST-01            |
| ----- | ---------------- |
| 対象機能  | 姿勢記録～表示までの一連のフロー |
| 操作手順  |                  |

1. アプリ起動
2. ホーム画面で「朝」「普通」「コメント：テストです」と入力
3. 「記録する」ボタンをクリック
4. カレンダー画面に遷移
5. 該当日をクリックして記録確認 |
   \| 期待結果     |

* トースト or アラートで「記録完了」が表示
* カレンダーに「普通」が反映されている
* 詳細表示で正しい記録内容が確認できる |

---

### 📄 テストケース：記録のグラフ反映

| テストID | ST-02                        |
| ----- | ---------------------------- |
| 前提    | 過去1週間の姿勢データを入力済み（DB上）        |
| 操作    | ホーム → グラフ画面 → 週グラフ表示切替       |
| 期待結果  | 折れ線グラフまたは棒グラフで週別平均が描画されていること |

---

## 🛠 使用ライブラリ想定

| 種類       | ライブラリ                |
| -------- | -------------------- |
| 単体/結合テスト | JUnit 5 + AssertJ    |
| DBモック    | SQLite (テスト用ファイルDB)  |
| UI自動化    | TestFX（JavaFX UIテスト） |
| データセット管理 | DBUnit またはプレーンSQL    |

---

## 📌 テスト環境準備のポイント

* `test/resources` に SQLite のテンプレートDBを設置
* テスト開始前に DB 初期化 → テスト実行 → 終了後クリーンアップ
* UIテストは画面が立ち上がるためCI/CDとの相性に注意（Headlessモード対応推奨）

---

以上が、Posture Correction Project の **結合テスト / システムテスト**設計です。

---
