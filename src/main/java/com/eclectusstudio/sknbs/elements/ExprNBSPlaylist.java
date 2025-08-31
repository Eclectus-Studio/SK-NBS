package com.eclectusstudio.sknbs.elements;

import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import org.bukkit.event.Event;

import static ch.njol.skript.Skript.registerExpression;

public class ExprNBSPlaylist extends SimpleExpression<Playlist> {

    static {
        registerExpression(ExprNBSPlaylist.class, Playlist.class, ExpressionType.SIMPLE,
                "nbs playlist from %songs%");
    }

    private Expression<Song> songs;

    @Override
    protected Playlist[] get(Event e) {
        Song[] parsed = songs.getAll(e);
        if (parsed.length == 0) return null;
        return new Playlist[]{new Playlist(parsed)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Playlist> getReturnType() {
        return Playlist.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "nbs playlist from " + songs.toString(e, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        songs = (Expression<Song>) exprs[0];
        return true;
    }
}
