package com.hfad.newaplicationfirebasetest.data;

import java.util.HashMap;
import java.util.Map;

public class NoteDataMapping {

    public class Fields {
        public final static String NAME = "name";
        public final static String DESCRIPTION = "description";
    }

    //метод на чтение данных из БД
    public static Note toNote(String id, Map<String, Object> doc) {
        Note note = new Note((String) doc.get(Fields.NAME), (String) doc.get(Fields.DESCRIPTION));
        note.setId(id);
        return note;
    }

    public static Map<String, Object> toDocument(Note note) {
        Map<String, Object> mapNote = new HashMap<>();
        mapNote.put(Fields.NAME, note.getName());
        mapNote.put(Fields.DESCRIPTION, note.getDescription());
        return mapNote;
    }
}
