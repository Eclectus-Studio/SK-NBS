package com.eclectusstudio.sknbs.elements;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import static ch.njol.skript.Skript.registerExpression;

public class ExprPlayNBS extends SimpleExpression<SongPlayer> {

    static {
        registerExpression(ExprPlayNBS.class, SongPlayer.class, ch.njol.skript.lang.ExpressionType.SIMPLE,
                "play radio nbs %song/playlist% to %players%",
                "play position nbs %song/playlist% at %location% with range %number% to %players%",
                "play noteblock nbs %song/playlist% at %block% with range %number% to %players%",
                "play entity nbs %song/playlist% at %entity% with range %number% to %players%"
        );
    }

    private int pattern;
    private Expression<?> source;
    private Expression<?> extra;
    private Expression<Number> range;
    private Expression<Player> players;

    @Override
    protected SongPlayer[] get(Event e) {
        Object src = source.getSingle(e);
        Player[] plrs = players != null ? players.getArray(e) : new Player[0];
        if (src == null) return new SongPlayer[0];

        SongPlayer sp = null;

        switch (pattern) {
            case 0: // Radio
                if (src instanceof Song s0) sp = new RadioSongPlayer(s0);
                else if (src instanceof Playlist p0) sp = new RadioSongPlayer(p0);
                break;
            case 1: // Position
                if (!(extra.getSingle(e) instanceof Location loc1)) return new SongPlayer[0];
                Number r1 = range.getSingle(e);
                if (r1 == null) return new SongPlayer[0];
                if (src instanceof Song s1) sp = new PositionSongPlayer(s1);
                else if (src instanceof Playlist p1) sp = new PositionSongPlayer(p1);
                if (sp instanceof PositionSongPlayer psp1) {
                    psp1.setTargetLocation(loc1);
                    psp1.setDistance(r1.intValue());
                }
                break;
            case 2: // NoteBlock
                if (!(extra.getSingle(e) instanceof Block b2)) return new SongPlayer[0];
                Number r2 = range.getSingle(e);
                if (r2 == null) return new SongPlayer[0];
                if (src instanceof Song s2) sp = new NoteBlockSongPlayer(s2);
                else if (src instanceof Playlist p2) sp = new NoteBlockSongPlayer(p2);
                if (sp instanceof NoteBlockSongPlayer nbsp2) {
                    nbsp2.setNoteBlock(b2);
                    nbsp2.setDistance(r2.intValue());
                }
                break;
            case 3: // Entity
                if (!(extra.getSingle(e) instanceof Entity ent3)) return new SongPlayer[0];
                Number r3 = range.getSingle(e);
                if (r3 == null) return new SongPlayer[0];
                if (src instanceof Song s3) sp = new EntitySongPlayer(s3);
                else if (src instanceof Playlist p3) sp = new EntitySongPlayer(p3);
                if (sp instanceof EntitySongPlayer esp3) {
                    esp3.setEntity(ent3);
                    esp3.setDistance(r3.intValue());
                }
                break;
        }

        if (sp != null && plrs.length > 0) {
            for (Player p : plrs) sp.addPlayer(p);
        }

        if (sp != null) sp.setPlaying(true);

        return sp != null ? new SongPlayer[]{sp} : new SongPlayer[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends SongPlayer> getReturnType() {
        return SongPlayer.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "play NBS song/playlist with type " + pattern + " to specified players";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.pattern = matchedPattern;
        switch (matchedPattern) {
            case 0:
                source = exprs[0];
                players = (Expression<Player>) exprs[1];
                break;
            case 1:
            case 2:
            case 3:
                source = exprs[0];
                extra = exprs[1];
                range = (Expression<Number>) exprs[2];
                players = (Expression<Player>) exprs[3];
                break;
        }
        return true;
    }
}
