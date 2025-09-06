
🎵 **skNBS – Skript Addon for NoteBlockAPI**
---
skNBS is a Skript addon that integrates NoteBlockAPI with Skript, allowing you to load .nbs songs, create playlists, and control playback directly from Skript.

📦 **Installation**
*   Install Skript on your Spigot/Paper server.
*   Install NoteBlockAPI.
*   Place `sknbs.jar` into your `/plugins` folder.
*   Restart the server.

📖 **Syntax & Usage**
---
🎶 **Songs**
Load an `.nbs` song from the `plugins/NoteBlockAPI/songs` folder:
`set {_song} to nbs song from "test.nbs"`

📑 **Playlists**
Create a playlist from multiple songs:
`set {_song1} to nbs song from "intro.nbs"`
`set {_song2} to nbs song from "loop.nbs"`
`set {_playlist} to nbs playlist from {_song1} and {_song2}`

▶️ **Playback Types**
*   **Radio**: Plays directly to players (no location).
    *   `play radio nbs {_song} to all players`
    *   `play radio nbs {_playlist} to player`
*   **Position**: Plays from a location in the world with a range.
    *   `play position nbs {_song} at location of player with range 32 to all players`
*   **NoteBlock**: Plays from a specific NoteBlock.
    *   `play noteblock nbs {_song} at target block with range 16 to {_p}`
*   **Entity**: Plays from an entity (follows the entity).
    *   `play entity nbs {_playlist} at {_mob} with range 24 to all players`

⏯️ **Controls**
Pause, resume, or stop a SongPlayer:
*   `pause nbs {_player}`
*   `resume nbs {_player}`
*   `stop nbs {_player}`

⏩ **Seeking**
Skip forwards or backwards in a song/playlist by seconds:
*   `forward 10 seconds in nbs {_player}` `# skip ahead 10s`
*   `rewind 5 seconds in nbs {_player}` `# rewind 5s`

⚠️ **Notes**
*   Songs must be placed in `/plugins/NoteBlockAPI/songs/`.
*   SongPlayer objects are returned when you start playback, so keep them in variables if you want to pause/seek later.
*   Playlists currently don’t support seeking between songs.

💡 **Example Script**
```skript
command /testradio:
    trigger:
        # Load the song
        set {_song} to nbs song from "dj.nbs"

        # Play song as radio and store the SongPlayer
        set {_player} to play radio nbs {_song} to player
        send "&aNow playing DJ song as radio!" to player

        # Wait 10 seconds, then pause
        wait 10 seconds
        pause nbs {_player}
        send "&ePaused the song!" to player

        # Wait 3 seconds, then resume
        wait 3 seconds
        resume nbs {_player}
        send "&aResumed the song!" to player

        # Wait 5 seconds, then skip forward 10 seconds
        wait 5 seconds
        forward 10 seconds in nbs {_player}
        send "&bSkipped forward 10 seconds!" to player

        # Wait 5 seconds, then rewind 1 second
        wait 5 seconds
        rewind 1 seconds in nbs {_player}
        send "&bRewinded 1 second!" to player

        # Wait 5 seconds, then stop the song
        wait 5 seconds
        stop nbs {_player}
        send "&cStopped the song!" to player
```

🛠️ API Reference
Expressions
*   `nbs song from %string% → Song`
*   `nbs playlist from %songs% → Playlist`
*   `Effects`
*   `play radio nbs %song/playlist% to %players%`
*   `play position nbs %song/playlist% at %location% with range %number% to %players%`
*   `play noteblock nbs %song/playlist% at %block% with range %number% to %players%`
*   `play entity nbs %song/playlist% at %entity% with range %number% to %players%`
*   `pause nbs %songplayer%`
*   `resume nbs %songplayer%`
*   `stop nbs %songplayer%`
*   `forward %number% seconds in nbs %songplayer%`
*   `rewind %number% seconds in nbs %songplayer%`
