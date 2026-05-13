# Keyring Demo

This directory contains a Tauri v2.0 cross-platform app that serves two purposes:

- It provides sample code for developers on how to integrate the keyring ecosystem into their apps.
- It allows users and developers both with the ability to poke around in the keyring-compatible stores used by their apps.

You can download releases of the app using the [info about installation](#installation-instructions) below.

The documentation for using the app is on the [Keyring ecosystem wiki](https://github.com/open-source-cooperative/keyring-rs/wiki/Keyring). This document provides instructions for how to install and to build the app for various platforms.

***

## Installation Instructions

Pre-built installers for this app are available for all supported platforms. (The macOS installer is available both for direct download and via the macOS App Store.)

### Linux/Windows/macOS

Builds for all three of these platforms can be [downloaded from CrabNebula](https://web.crabnebula.cloud/brotskydotcom/keyring-demo/releases).

### iOS/macOS

You will be able to get Keyring Demo through the App Store once it’s approved. For now, you can get the public beta at [this link](). The version that’s available from the App Store is a sandboxed app, so on macOS it has access to both to Keychain Services credentials and to Protected Data credentials.

Note: The App Store macOS version runs on Apple Silicon machines only. It’s possible to build the app for the older x86 machines, but because those machines don’t have protected data you will only have access to the Keychain.

### Android

There is a Google Play private beta build of Keyring Demo available by sending email to `keyring-demo` at domain `brotsky.com`. Once we get enough users of that build, it will update to being a public beta and (eventually) released.

## Build Instructions

This list is organized by distribution channel rather than by platform.

### Apple App Store

In order to distribute through the Apple App Store, you must be an Apple Developer. The Tauri 2.0 documentation has tons of information about how developers configure their builds so they are signed correctly for App Store upload. The configuration files in this repo have all been configured correctly for the `brotskydotcom` App Store developer, team ID `85H73V9R3F`. In order for you to publish this app under your own team ID, you will need to alter all the configuration files to use a bundle ID that *you* have registered under your own team ID, to refer to your own signing certificates, and so on.

To build the iOS app for the app store, follow these steps:

1. (only needed if on a pre-release version) Edit the `src-tauri/taur.conf.json` file and *temporarily* remove any pre-release designation after the main version number. (The main version number can only be numeric for iOS.) In the last step you will restore this designator.

2. Edit the `src-tauri/tauri.conf.json` file and increment the last digit of the `iOS > bundleVersion` parameter (let’s say it was 7 and so it becomes 8).

3. Give this build command:
   ```shell
   npm run tauri ios build
   ```

   This will build the iOS `ipa` of the the app but, more importantly, it will update the `Info.plist` of the app to have the new build number (i.e., `CFBundleVersion`).

4. Now give this build command:
   ```shell
   npm run tauri ios build -- --open
   ```

   This will open Xcode on the Mac project, allowing you to set the build target to `Any iOS device` and then build via `Archive`. That will open the organizer window on the latest build, which can then be uploaded to the app store.

5. (only needed if on a pre-release version) Put the pre-release version designator back (see step 1).

To build the macOS app for the app store, follow these steps (after having fixed all the configuration files):

1. (only needed if on a pre-release version) Edit the `src-tauri/tauri.config.json` file and *temporarily* remove any pre-release designation after the main version number. (The main version number can only be numeric for macOS.) In the last step you will restore this designator.

2. Edit the `src-tauri/tauri.conf.json` file and change the `macOS > bundleVersion` parameter to be one patch version greater than it was. So, for example, if it was `1.0.4`, you would change it to `1.0.5`. This ensures that the build number is higher than the one last uploaded to the app store, which is a requirement.

3. Edit the `src-tauri/tauri.appstore.conf.json` file and change the path value of the `embedded.provisionprofile` to point to your App Store Mac 3rd party developer installer provisioning profile.

4. Give this sequence of commands, replacing `85H73V9R3F` with your Team ID, `SSD3DPQ9MU` with your AppStoreConnect API Key ID, `69a6de7e-5cea-47e3-e053-5b8c7c11a4d1` with your AppStoreConnect API Issuer ID, and `1606491991` with the Apple Id found in the the App Information section for your registered application in AppStoreConnect:
   ```shell
   npm run tauri build -- --no-bundle
   npm run tauri bundle -- --bundles app --config src-tauri/tauri.appstore.conf.json
   pushd src-tauri/target/release/bundle/macos
   xcrun productbuild --sign "85H73V9R3F" --component "keyring-demo.app" /Applications "keyring-demo.pkg"
   xcrun altool --upload-app --type macos --file "keyring-demo.pkg" --apiKey SSD3DPQ9MU --apiIssuer 69a6de7e-5cea-47e3-e053-5b8c7c11a4d1 --apple-id 1606491991
   popd
   ```

5. (only needed if on a pre-release version) Put the pre-release version designator back (see step 1).

### Google Play

In order to distribute Android apps through Google Play you must be a Google Play Developer. The Tauri 2.0 documentation has tons of information about how developers configure their builds so they are signed correctly for Google Play upload. The configuration files in this repo have all been configured correctly for the `brotskydotcom` Google Play developer, org ID `85H73V9R3F`. And there is an additional file that has to be configured, `src-tauri/gen/android/keystore.properties`, that is not checked in: this has the properties needed for signing using your created keystore.

To build the signed app bundle for Android, follow these steps:

1. Increment the Android bundle version code by 1 in `src-tauri/tauri-config.json`

2. Give this build command:
   ```shell
   npm run tauri android build --aab true
   ```

Now you can open the `src-tauri/gen/android/app/build/outputs/bundle/universalRelease` folder and drag the built `.aab` bundle into a new release on the Google Play Console.

### CrabNebula

There are two steps to releasing on CrabNebula: building your bundles, and then uploading them to a release.

For building on Mac, Win, and Linux, see the GitHub `build-tauri` workflow in this repository.

To release anything on CrabNebula, you first have to register with them and create a project. Having done that, follow these steps:

* Set up the environment variable `CN_API_KEY` to have your API key.
  
* Create a release with this command (changing the release name and the notes, obviously):
  
  ```shell
   cn release draft brotskydotcom/keyring-demo "v1.0.0-beta.2" --notes "This is the second beta of the Keyring Demo app. It's designed to layout well on phone as well as tablet and desktop."
  ```
  
  Be sure to note the `id` of the generated release (printed to stdout). You will need it in the upload commands.
  
* Having generated your bundles, upload them using this command:
  ```shell
  cn release upload brotskydotcom/keyring-demo <id> --framework tauri
  ```

  where `<id>` is the `id` generated when the release draft was created.

## License

Licensed under either of

* Apache License, Version 2.0, ([LICENSE-APACHE](LICENSE-APACHE) or http://www.apache.org/licenses/LICENSE-2.0)
* MIT license ([LICENSE-MIT](LICENSE-MIT) or http://opensource.org/licenses/MIT)

at your option.

### Contribution

Unless you explicitly state otherwise, any contribution intentionally submitted for inclusion in the work by you, as defined in the Apache-2.0 license, shall be dual licensed as above, without any additional terms or conditions.
