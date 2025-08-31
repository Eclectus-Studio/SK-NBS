package com.eclectusstudio.sknbs.elements;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.event.Event;

import static ch.njol.skript.Skript.registerEffect;

public class EffPauseResume extends Effect {

    static {
        registerEffect(EffPauseResume.class,
                "pause nbs %songplayer%",
                "resume nbs %songplayer%",
                "stop nbs %songplayer%");
    }

    private Expression<SongPlayer> sp;
    private int pattern;

    @Override
    protected void execute(Event e) {
        SongPlayer p = sp.getSingle(e);
        if (p == null) return;
        switch (pattern) {
            case 0 -> p.setPlaying(false);
            case 1 -> p.setPlaying(true);
            case 2 -> { p.setPlaying(false); p.destroy(); }
        }
    }

    @Override public String toString(Event e, boolean debug) { return "pause/resume/stop nbs player"; }

    @Override @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        sp = (Expression<SongPlayer>) exprs[0];
        pattern = matchedPattern;
        return true;
    }
}
