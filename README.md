<h1 align="center">Jellyfin for Android</h1>
<h3 align="center">Part of the <a href="https://jellyfin.org">Jellyfin Project</a></h3>

---

<p align="center">
<img alt="Logo Banner" src="https://raw.githubusercontent.com/jellyfin/jellyfin-ux/master/branding/SVG/banner-logo-solid.svg?sanitize=true"/>
<br/>
<br/>
<a href="https://github.com/jellyfin/jellyfin-android">
<img alt="GPL 2.0 License" src="https://img.shields.io/github/license/jellyfin/jellyfin-android.svg"/>
</a>
<a href="https://github.com/jellyfin/jellyfin-android/releases">
<img alt="Current Release" src="https://img.shields.io/github/release/jellyfin/jellyfin-android.svg"/>
</a>
<a href="https://translate.jellyfin.org/projects/jellyfin-android/jellyfin-android/">
<img alt="Translation Status" src="https://translate.jellyfin.org/widgets/jellyfin-android/-/jellyfin-android/svg-badge.svg"/>
</a>
<br/>
<a href="https://opencollective.com/jellyfin">
<img alt="Donate" src="https://img.shields.io/opencollective/all/jellyfin.svg?label=backers"/>
</a>
<a href="https://features.jellyfin.org">
<img alt="Feature Requests" src="https://img.shields.io/badge/fider-vote%20on%20features-success.svg"/>
</a>
<a href="https://matrix.to/#/+jellyfin:matrix.org">
<img alt="Chat on Matrix" src="https://img.shields.io/matrix/jellyfin:matrix.org.svg?logo=matrix"/>
</a>
<a href="https://www.reddit.com/r/jellyfin/">
<img alt="Join our Subreddit" src="https://img.shields.io/badge/reddit-r%2Fjellyfin-%23FF5700.svg"/>
</a>
<br/>
<a href="https://play.google.com/store/apps/details?id=org.jellyfin.mobile">
<img width="153" src="https://jellyfin.org/images/store-icons/google-play.png" alt="Jellyfin on Google Play"/>
</a>
<a href="https://www.amazon.com/gp/aw/d/B081RFTTQ9">
<img width="153" src="https://jellyfin.org/images/store-icons/amazon.png" alt="Jellyfin on Amazon Appstore"/>
</a>
<a href="https://f-droid.org/en/packages/org.jellyfin.mobile/">
<img width="153" src="https://jellyfin.org/images/store-icons/fdroid.png" alt="Jellyfin on F-Droid"/>
</a>
<br/>
<a href="https://repo.jellyfin.org/releases/client/android/">Download archive</a>
</p>

Jellyfin Mobile is an Android app that connects to Jellyfin instances and integrates with the [official web client](https://github.com/jellyfin/jellyfin-web).
We welcome all contributions and pull requests! If you have a larger feature in mind please open an issue so we can discuss the implementation before you start.
Even though the client is only a web wrapper there are still lots of improvements and bug fixes that can be accomplished with Android and Kotlin knowledge.

Most of the translations can be found in the [web client](https://translate.jellyfin.org/projects/jellyfin/jellyfin-web) since it's the base for the Android client as well. Translations for the app can also be improved very easily from our [Weblate](https://translate.jellyfin.org/projects/jellyfin-android/jellyfin-android) instance. Look through the following graphic to see if your native language could use some work!

<a href="https://translate.jellyfin.org/engage/jellyfin-android/">
<img alt="Detailed Translation Status" src="https://translate.jellyfin.org/widgets/jellyfin-android/-/jellyfin-android/multi-auto.svg"/>
</a>

This client was rewritten from scratch with a fresh git history in July to August 2020, and replaces the old Cordova-based client,
which can still be found [in the archives](https://github.com/jellyfin-archive/jellyfin-android-original).

## Build Process

### Dependencies

- Android SDK

### Build

1. Clone or download this repository

   ```sh
   git clone https://github.com/jellyfin/jellyfin-android.git
   cd jellyfin-android
   ```

2. Open the project in Android Studio and run it from there or build an APK directly through Gradle:

   ```sh
   ./gradlew assembleDebug
   ```

### Deploy to device/emulator

   ```sh
   ./gradlew installDebug
   ```

*You can also replace the "Debug" with "Release" to get an optimized release binary.*

## Release Flavors

There are two flavors (variants) of the Jellyfin for Android app:

- The **proprietary** version comes with Google Chromecast support
- The **libre** version comes without Google Chromecast support

The proprietary version is available on [Google Play](https://play.google.com/store/apps/details?id=org.jellyfin.mobile) and the [Amazon Appstore](https://www.amazon.com/gp/aw/d/B081RFTTQ9), while the libre version is available on [F-Droid](https://f-droid.org/en/packages/org.jellyfin.mobile/).
Additionally, `beta` releases exist for both flavors, but only the proprietary version is published to a beta track on [Google Play](https://play.google.com/store/apps/details?id=org.jellyfin.mobile).
If you'd like to test the beta outside of Google Play, you can simply download it from the [GitHub releases](https://github.com/jellyfin/jellyfin-android/releases/latest).

---

## 关于此 Fork

本项目 Fork 自 [jellyfin/jellyfin-android](https://github.com/jellyfin/jellyfin-android)，增加了 **IP4P 地址支持**，允许通过 [natmap](https://github.com/heiher/natmap) 的 NAT 穿透方案直连 Jellyfin 服务器，无需公网 IP 或端口转发。

### 主要更改

| 文件 | 更改说明 |
|------|---------|
| `utils/Ip4pParser.kt` | **新增** — IP4P 地址检测与解码（`2001::port:hi16:lo16` → `http://ip:port`） |
| `utils/Ip4pResolver.kt` | **新增** — DNS AAAA 记录解析，支持域名 → IP4P 地址的自动发现 |
| `ui/.../ServerSelection.kt` | IP4P 模式开关，开启后跳过服务器探测，直接解码连接。输入时实时预览解码结果 |
| `ui/.../ServerSuggestion.kt` | 已存 IP4P 服务器显示 `[IP4P]` 标签 |
| `ui/.../ConnectScreen.kt` | 传递 IP4P 标志到 ViewModel |
| `data/entity/ServerEntity.kt` | 新增 `isIp4p` 字段，持久化 IP4P 服务器类型 |
| `data/JellyfinDatabase.kt` | 数据库版本 5→6，自动迁移 |
| `data/dao/ServerDao.kt` | `insert()` 支持 `isIp4p` 参数 |
| `app/ApiClientController.kt` | 存储原始域名，每次重连时重新 DNS 解析（适配 NAT 映射变化） |
| `webapp/WebViewFragment.kt` | IP4P 域名异步解析后加载；连接失败时自动重新解析并重试一次 |
| `MainViewModel.kt` | `switchServer()` 增加 `isIp4p` 参数 |
| `res/values/strings.xml` | 新增 IP4P 相关字符串资源 |

### IP4P 地址格式

```
2001::{port}:{ipv4-hi16}:{ipv4-lo16}

示例: 2001::1f90:cb00:7101
  → IPv4: 203.0.113.1
  → 端口: 8080
  → URL:  http://203.0.113.1:8080
```

### 使用方法

1. 在服务器端部署 [natmap](https://github.com/heiher/natmap)，配置 DNS 更新脚本
2. 在 App 的服务器地址栏输入域名（或原始 IP4P 地址）
3. 打开 **IP4P address** 开关
4. 点击 **Connect** — App 通过 DNS AAAA 解析域名，解码 IP4P 地址，直连服务器
5. 服务器列表中会显示 `[IP4P]` 标签，重连时自动重新 DNS 解析

### 前置条件

- 服务器端运行 [natmap](https://github.com/heiher/natmap)
- 所有 NAT 层必须为全锥形（NAT-1）
- DNS AAAA 记录已更新为 IP4P 地址（或手动输入原始 IP4P 地址）

## 开源协议

本项目基于 [Jellyfin for Android](https://github.com/jellyfin/jellyfin-android)，沿用其 [GPL-2.0](LICENSE.md) 开源协议。新增代码同样以 GPL-2.0 协议发布。
