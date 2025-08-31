# 🎵 skNBS – Skript Addon for NoteBlockAPI

skNBS is a Skript addon that integrates the NoteBlockAPI with Skript, allowing you to load `.nbs` songs, create playlists, and control playback directly from Skript.

## 📦 Installation
1.  Install Skript on your Spigot/Paper server.
2.  Install NoteBlockAPI.
3.  Place the `skNBS.jar` into your `/plugins` folder.
4.  Restart the server.

## 📖 Syntax & Usage

### 🎶 Songs
Load an `.nbs` song from the `plugins/NoteBlockAPI/songs` folder:
Use code with caution.
```
set {_song} to nbs song from "test.nbs"
```
### 📑 Playlists
Create a playlist from multiple songs:
set {_song1} to nbs song from "intro.nbs"
set {_song2} to nbs song from "loop.nbs"
set {_playlist} to nbs playlist from {_song1} and {_song2}

### ▶️ Playback Types
*   **Radio:** Plays to players directly (no location).
    ```
    play radio nbs {_song} to all players
    play radio nbs {_playlist} to player
    ```
*   **Position:** Plays from a location in the world with a range.
    ```
    play position nbs {_song} at location of player with range 32 to all players
    ```
*   **NoteBlock:** Plays from a specific NoteBlock.
    ```
    play noteblock nbs {_song} at target block with range 16 to {_p}
    ```
*   **Entity:** Plays from an entity (follows the entity).
    ```
    play entity nbs {_playlist} at {_mob} with range 24 to all players
    ```

### ⏯ Controls
Pause, resume, or stop a SongPlayer:
pause nbs {_player}
resume nbs {_player}
stop nbs {_player}

### ⏩ Seeking
Skip forwards or backwards in a song/playlist by seconds:
forward 10 seconds in nbs {_player} # skip ahead 10s
rewind 5 seconds in nbs {_player} # rewind 5s

## ⚠️ Notes
*   Songs must be placed in `/plugins/NoteBlockAPI/songs/`.
*   `SongPlayer` objects are returned when you start playback, so keep them in variables if you want to pause/seek later.
*   Playlists currently don’t support seeking between songs.

## 💡 Example Script
```
command /music:
    trigger:
        set {_song} to nbs song from "lobby.nbs"
        play radio nbs {_song} to player
        send "&aNow playing lobby music!" to player

command /dj:
    trigger:
        forward 30 seconds in nbs {_songplayer}
```

## 🛠 API Reference

### Expressions
*   `nbs song from %string% → Song`
*   `nbs playlist from %songs% → Playlist`

### Effects
*   `play radio nbs %song/playlist% to %players%`
*   `play position nbs %song/playlist% at %location% with range %number% to %players%`
*   `play noteblock nbs %song/playlist% at %block% with range %number% to %players%`
*   `play entity nbs %song/playlist% at %entity% with range %number% to %players%`
*   `pause nbs %songplayer%`
*   `resume nbs %songplayer%`
*   `stop nbs %songplayer%`
*   `forward %number% seconds in nbs %songplayer%`
*   `rewind %number% seconds in nbs %songplayer%`
