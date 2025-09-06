package com.eclectusstudio.sknbs;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;
import ch.njol.yggdrasil.Fields;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

public class Types {
    public static void register() {
        // --- Song ---
        Classes.registerClass(new ClassInfo<Song>(Song.class, "song")
                .user("songs?")
                .name("NBS Song")
                .description("Represents a NoteBlockAPI Song.")
                .parser(new Parser<Song>() {
                    public boolean canParse(String s) { return false; }
                    public Song parse(String s, Kleenean kleenean) { return null; }
                    public String toString(Song song, int flags) {
                        return "song(" + (song != null ? song.getPath() : "null") + ")";
                    }
                    public String toVariableNameString(Song song) {
                        return "song:" + song.hashCode();
                    }
                    public String getVariableNamePattern() {
                        return "song:.+";
                    }
                })
                .serializer(new Serializer<Song>() {
                    @Override
                    public Fields serialize(Song song) throws NotSerializableException {
                        return null; // runtime only, not saved
                    }

                    @Override
                    public void deserialize(Song song, Fields fields) throws StreamCorruptedException, NotSerializableException {
                        throw new NotSerializableException("Songs cannot be deserialized (runtime only).");
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return false;
                    }

                    @Override
                    protected boolean canBeInstantiated() {
                        return false;
                    }
                })
        );

        // --- Playlist ---
        Classes.registerClass(new ClassInfo<Playlist>(Playlist.class, "playlist")
                .user("playlists?")
                .name("NBS Playlist")
                .description("Represents a NoteBlockAPI Playlist.")
                .parser(new Parser<Playlist>() {
                    public boolean canParse(String s) { return false; }
                    public Playlist parse(String s, Kleenean kleenean) { return null; }
                    public String toString(Playlist pl, int flags) {
                        return "playlist(" + (pl != null ? pl.getSongList().size() : 0) + " songs)";
                    }
                    public String toVariableNameString(Playlist pl) {
                        return "playlist:" + pl.hashCode();
                    }
                    public String getVariableNamePattern() {
                        return "playlist:.+";
                    }
                })
                .serializer(new Serializer<Playlist>() {
                    @Override
                    public Fields serialize(Playlist playlist) throws NotSerializableException {
                        return null; // runtime only
                    }

                    @Override
                    public void deserialize(Playlist pl, Fields fields) throws StreamCorruptedException, NotSerializableException {
                        throw new NotSerializableException("Playlists cannot be deserialized (runtime only).");
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return false;
                    }

                    @Override
                    protected boolean canBeInstantiated() {
                        return false;
                    }
                })
        );
        // --- SongPlayer ---
        Classes.registerClass(new ClassInfo<SongPlayer>(SongPlayer.class, "songplayer")
                .user("songplayers?")
                .name("NBS SongPlayer")
                .description("Represents a NoteBlockAPI SongPlayer (a currently playing song).")
                .parser(new ch.njol.skript.classes.Parser<SongPlayer>() {
                    public boolean canParse(String s) { return false; }
                    public SongPlayer parse(String s, Kleenean kleenean) { return null; }
                    public String toString(SongPlayer sp, int flags) {
                        return "songplayer(" + (sp != null ? sp.getSong() : "null") + ")";
                    }
                    public String toVariableNameString(SongPlayer sp) {
                        return "songplayer:" + sp.hashCode();  // <-- This is the method in question
                    }
                    public String getVariableNamePattern() { return "songplayer:.+"; }
                })
                .serializer(new Serializer<SongPlayer>() {
                    @Override
                    public Fields serialize(SongPlayer sp) throws NotSerializableException { return null; }
                    @Override
                    public void deserialize(SongPlayer sp, Fields fields) throws StreamCorruptedException, NotSerializableException {
                        throw new NotSerializableException("SongPlayers cannot be deserialized (runtime only).");
                    }
                    @Override
                    public boolean mustSyncDeserialization() { return false; }
                    @Override
                    protected boolean canBeInstantiated() { return false; }
                })
        );

    }
}
