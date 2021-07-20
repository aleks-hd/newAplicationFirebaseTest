package com.hfad.newaplicationfirebasetest.data;

import java.util.Map;

public class NoteDataMapping {

    public class Fields{
        public final static String NAME = "name";
        public final static String DESCRIPTION = "description";
    }
//метод на чтение данных из БД
    public static Note toNote(String id, Map<String, Object> doc){
        Note note = new Note();
        note.setName((String) doc.get(Fields.NAME));
        note.setDescription((String)doc.get(Fields.DESCRIPTION));
        note.setId(id);
        return  note;
    }

}
