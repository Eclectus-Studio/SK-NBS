package com.eclectusstudio.sknbs.elements;

import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import org.bukkit.event.Event;

import java.io.File;

import static ch.njol.skript.Skript.registerExpression;

public class ExprNBSSong extends SimpleExpression<Song> {

    static {
        registerExpression(ExprNBSSong.class, Song.class, ExpressionType.SIMPLE,
                "nbs song from %string%");
    }

    private Expression<String> filePath;

    @Override
    protected Song[] get(Event e) {
        String path = filePath.getSingle(e);
        if (path == null) return null;
        try {
            File songFile = new File("plugins/NoteBlockAPI/songs", path);
            Song song = NBSDecoder.parse(songFile);
            if (song == null) return null;
            return new Song[]{song};
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Song> getReturnType() {
        return Song.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "nbs song from " + filePath.toString(e, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        filePath = (Expression<String>) exprs[0];
        return true;
    }
}
