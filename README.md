# HoloTube - Improved Youtube Live Player for Hololive

A project focused on implementing a better user interface for youtube live streams and making it easier to follow Hololive talents for Android 19+.

<div style="display: inline-block">
<img src="https://i.imgur.com/TgX78k8.png" alt="live_image" width="33%" /> 
<img src="https://i.imgur.com/Y9pqjna.png" alt="upcoming_image" width="33%"/>
 <img src="https://i.imgur.com/yzphKen.png" alt="following_image" width="33%"/>
</div>

## Motivation

The current Youtube live stream player has one major flaw. As of 6/2/2021, when the chat is turned on while in landscape mode it covers most of the screen. This can be seen in the following screenshots.

<div style="display: inline-block">
<img src="https://i.imgur.com/uee92x6.png" alt="example 1" width="49%"/> 
<img src="https://i.imgur.com/q9r81Fc.jpg" alt="example 2" width="49%"/>
</div>

</br>
Notice how the chat and donations blocks half of the content. This purpose of this app is to address this problem and make following Hololive streams much easier.

## Implementation

This app implements an user interface similar to the [twitch app](https://play.google.com/store/apps/details?id=tv.twitch.android.app&hl=en_CA&gl=US) for a better viewing experience.

<div style="display: inline-block">
<img src="https://i.imgur.com/JsZ6ZYZ.png" alt="app_live_screen" width="49%">
<img src="https://i.imgur.com/C2WDwlw.png" alt="app_live_screen" width="49%">
</div>

## Features
* Chat displayed **_beside_** stream instead of directly over
* View all live streams from Hololive talents
* Keep up with all the scheduled streams
* Ability to follow all your favourite streamers 

## Possible Improvements
* Get notification when followed streamer is live
* Ability to type in chat instead of it being read on only
* Integrate youtube and youtube OAuth api
* Use MVVM structure for app instead of viewmodel communicating directly with data
* Add search functionality 

## Open-source Libraries Used
* [Glide](https://github.com/bumptech/glide) for displaying thumbnail and profile pictures
* [HoloAPI](https://github.com/holofans/holoapi) a REST api for Hololive streamer data
* [Retrofit2](https://github.com/square/retrofit) for requesting data from the REST api
* [Moshi](https://github.com/square/moshi/) for parsing JSON data 
* AndroidX and JetPack for ViewModels, Navigation, Toolbars, etc...

