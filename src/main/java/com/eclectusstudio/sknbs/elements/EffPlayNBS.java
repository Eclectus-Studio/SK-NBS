package com.eclectusstudio.sknbs.elements;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.*;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Arrays;

import static ch.njol.skript.Skript.registerEffect;

public class EffPlayNBS extends Effect {

    static {
        registerEffect(EffPlayNBS.class,
                // play types
                "play radio nbs %song/playlist% to %players%",
                "play position nbs %song/playlist% at %location% with range %number% to %players%",
                "play noteblock nbs %song/playlist% at %block% with range %number% to %players%",
                "play entity nbs %song/playlist% at %entity% with range %number% to %players%"
        );
    }

    private int pattern;
    private Expression<?> source;     // Song or Playlist
    private Expression<?> extra;      // Location / Block / Entity
    private Expression<Number> range; // Distance
    private Expression<Player> players;

    @Override
    protected void execute(Event e) {
        Object src = source.getSingle(e);
        if (src == null) return;

        SongPlayer sp = null;

        switch (pattern) {
            case 0: { // radio
                if (src instanceof Song s) sp = new RadioSongPlayer(s);
                else if (src instanceof Playlist p) sp = new RadioSongPlayer(p);
                break;
            }
            case 1: { // position
                Location loc = (Location) extra.getSingle(e);
                Number r = range.getSingle(e);
                if (loc == null || r == null) return;
                if (src instanceof Song s) sp = new PositionSongPlayer(s);
                else if (src instanceof Playlist p) sp = new PositionSongPlayer(p);
                if (sp instanceof PositionSongPlayer psp) {
                    psp.setTargetLocation(loc);
                    psp.setDistance(r.intValue());
                }
                break;
            }
            case 2: { // noteblock
                Block b = (Block) extra.getSingle(e);
                Number r = range.getSingle(e);
                if (b == null || r == null) return;
                if (src instanceof Song s) sp = new NoteBlockSongPlayer(s);
                else if (src instanceof Playlist p) sp = new NoteBlockSongPlayer(p);
                if (sp instanceof NoteBlockSongPlayer nbsp) {
                    nbsp.setNoteBlock(b);
                    nbsp.setDistance(r.intValue());
                }
                break;
            }
            case 3: { // entity
                Entity ent = (Entity) extra.getSingle(e);
                Number r = range.getSingle(e);
                if (ent == null || r == null) return;
                if (src instanceof Song s) sp = new EntitySongPlayer(s);
                else if (src instanceof Playlist p) sp = new EntitySongPlayer(p);
                if (sp instanceof EntitySongPlayer esp) {
                    esp.setEntity(ent);
                    esp.setDistance(r.intValue());
                }
                break;
            }
        }

        if (sp == null) return;

        // add all target players
        Player[] pls = players.getArray(e);
        Arrays.stream(pls).forEach(sp::addPlayer);

        // start playing
        sp.setPlaying(true);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "play NBS song/playlist with type " + pattern;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.pattern = matchedPattern;
        switch (matchedPattern) {
            case 0: // radio
                source = exprs[0];
                players = (Expression<Player>) exprs[1];
                break;
            case 1: // position
                source = exprs[0];
                extra = exprs[1];
                range = (Expression<Number>) exprs[2];
                players = (Expression<Player>) exprs[3];
                break;
            case 2: // noteblock
                source = exprs[0];
                extra = exprs[1];
                range = (Expression<Number>) exprs[2];
                players = (Expression<Player>) exprs[3];
                break;
            case 3: // entity
                source = exprs[0];
                extra = exprs[1];
                range = (Expression<Number>) exprs[2];
                players = (Expression<Player>) exprs[3];
                break;
        }
        return true;
    }
}
