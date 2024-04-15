# GitHubClient

## 説明

GitHubClientは、ユーザーがGitHubのユーザープロファイルや公開リポジトリを閲覧・探索できるように開発されたAndroidアプリケーションです。このアプリケーションは、クリーンで効率的なAndroidアプリケーションアーキテクチャ内で様々なGitHub APIの統合を示すことにより、シームレスで情報豊富なユーザーエクスペリエンスを提供することを重視しています。

## 機能

- **ユーザーリストビュー**: GitHubユーザーのスクロール可能なリストを動的に表示します。各リストアイテムには、ユーザーのアバターとユーザー名が表示されます。
- **詳細ユーザープロファイル**: ユーザーを選択すると、詳細なプロファイルページが表示されます。このページには、ユーザーのアバター、フルネーム、フォロワー数、フォロー数、および公開されている非フォークリポジトリの詳細なリストが含まれています。
- **リポジトリの詳細**: ユーザープロファイルの下にリストされているリポジトリのいずれかをクリックすると、WebViewを通じてリポジトリのより詳細な情報を表示でき、GitHubとの直接的な対話が可能になります。

#### <br/><br/>以下から最新のGitHubClientアプリのAPKをダウンロードしてください 👇
[![GitHubClient](https://img.shields.io/badge/GitHubClient-APK-blue)](https://github.com/Farhandroid/GithubClient/releases/download/v1.0.0/GitHubClient.apk)

## <br/><br/>GitHubClient アプリのアーキテクチャとモジュールの依存関係
<img width="403" alt="Screenshot 2024-04-15 at 19 12 19" src="https://github.com/Farhandroid/GithubClient/assets/32593150/ecf4ef43-aa5b-44d7-bd12-88e3d27a907e">

## <br/><br/>モジュールごとの単体テスト
<img width="1265" alt="Screenshot 2024-04-15 at 19 08 29" src="https://github.com/Farhandroid/GithubClient/assets/32593150/7f78f24a-7c99-4f64-8673-44eecf1b2083">
<img width="1048" alt="Screenshot 2024-04-15 at 19 08 16" src="https://github.com/Farhandroid/GithubClient/assets/32593150/b3e7905e-93ca-4618-acf9-ba0816a0204c">

## <br/><br/> モジュールの簡単な説明

- **Appモジュール**: `MainActivity`を含み、アプリの起点として機能します。ユーザーがアプリケーションとどのように対話し、ナビゲートするかを管理します。
- **Data**: API（ネットワーク通信）およびリポジトリ（データアクセスパターン）のサブモジュールを含み、すべてのデータ処理をカプセル化します。
- **Domain**: モデル（データエンティティ）、リポジトリ（データ操作のための抽象化）、およびuseCases（ビジネスロジック操作）を含み、コアを形成します。
- **Feature-User-List**: ユーザーリストと詳細機能に特化しており、UI（フラグメント）、ビジネスロジック（viewModel）、および統合を含みます。
- **Common**: `ResourceState` などのユーティリティクラスや共通のUI関連のKotlin拡張機能を含む `UiExtension.kt` がコードの再利用性と可読性を向上させるために含まれています。

アプリケーションは、ユニットテストによって強調される保守性と信頼性を優先します。


## <br/><br/>使用技術 🛠
- [Kotlin](https://kotlinlang.org/)
- [コルーチン](https://kotlinlang.org/docs/reference/coroutines-overview.html) 
- [Android Navigation Component](https://developer.android.com/guide/navigation)
- [Android アーキテクチャコンポーネント](https://developer.android.com/topic/libraries/architecture)
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Dagger-Hilt](https://dagger.dev/hilt/)
- [Retrofit](https://square.github.io/retrofit/)
- [Coil](https://coil-kt.github.io/coil/)
- [Android Material Components](https://github.com/material-components/material-components-android)
- [OkHttp](https://square.github.io/okhttp/)
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- [MockK](https://mockk.io/)
- [KSP (Kotlin Symbol Processing)](https://github.com/google/ksp)

## <br/><br/>デモ📸

https://github.com/Farhandroid/GithubClient/assets/32593150/ea6f0e3b-ab4d-4a3f-9f2e-e8cdb12f642e

## <br/><br/> このアプリの実行方法
- このリポジトリをクローンします
- [GitHub](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens) からアクセストークンを作成し、gradleファイルのAUTH_KEYを置き換えます
- アプリケーションをビルドします


