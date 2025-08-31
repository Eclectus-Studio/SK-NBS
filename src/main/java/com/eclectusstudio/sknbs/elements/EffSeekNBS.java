package com.eclectusstudio.sknbs.elements;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.event.Event;

import static ch.njol.skript.Skript.registerEffect;

public class EffSeekNBS extends Effect {

    static {
        registerEffect(EffSeekNBS.class,
                // + / - relative seeks (seconds)
                "forward %number% seconds in nbs %songplayer%",
                "rewind %number% seconds in nbs %songplayer%"
        );
    }

    private Expression<Number> seconds;
    private Expression<SongPlayer> sp;
    private int pattern; // 0 skip, 1 forward, 2 back, 3 rewind

    @Override
    protected void execute(Event e) {
        SongPlayer player = sp.getSingle(e);
        Number secNum = seconds.getSingle(e);
        if (player == null || secNum == null) return;

        double sec = secNum.doubleValue();
        Song current = resolveCurrentSong(player);

        // Fallback TPS if we can’t read the song (playlist edge cases, etc.)
        double tps = (current != null) ? current.getSpeed() : 20.0D;
        if (tps <= 0) tps = 20.0D;

        int deltaTicks = (int) Math.round(sec * tps);
        int cur = player.getTick();
        int target;

        switch (pattern) {
            case 0: // forward
                target = cur + deltaTicks;
                break;
            case 1: // rewind
                target = cur - deltaTicks;
                break;
            default:
                return;
        }

        short clamped = clampToSongBounds(player, current, target);
        player.setTick(clamped);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "seek/rewind/forward for NBS";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.pattern = matchedPattern;
        seconds = (Expression<Number>) exprs[0];
        sp = (Expression<SongPlayer>) exprs[1];
        return true;
    }

    // --- helpers ---

    private static Song resolveCurrentSong(SongPlayer player) {
        try {
            Song s = player.getSong();
            if (s != null) return s;
        } catch (Throwable ignored) { }
        try {
            Playlist pl = player.getPlaylist();
            if (pl != null && !pl.getSongList().isEmpty()) {
                return null; // playlists aren’t tracked here
            }
        } catch (Throwable ignored) { }
        return null;
    }

    private static short clampToSongBounds(SongPlayer player, Song song, int target) {
        int min = 0;
        int max;
        if (song != null) {
            max = Math.max(0, song.getLength() - 1);
        } else {
            max = Short.MAX_VALUE;
        }
        if (target < min) target = min;
        if (target > max) target = max;
        return (short) target;
    }
}
