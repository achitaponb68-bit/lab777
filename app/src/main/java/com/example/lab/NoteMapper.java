package com.example.lab;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class NoteMapper {

    private static final Gson gson = new Gson();

    // OOP -> Entity
    public static NoteEntity toEntity(Note note) {
        if (note == null) return null;

        if (note instanceof TextNote) {
            return new NoteEntity(
                    note.getTitle(),
                    "text",
                    null,
                    note.getContent(),
                    note.getCreatedDate()
            );
        } else if (note instanceof CheckListNote) {
            CheckListNote checkListNote = (CheckListNote) note;
            String jsonItems = gson.toJson(checkListNote.getCheckList());
            return new NoteEntity(
                    note.getTitle(),
                    "checklist",
                    jsonItems,
                    null,
                    note.getCreatedDate()
            );
        }
        return null;
    }

    // Entity -> OOP
    public static Note fromEntity(NoteEntity entity) {
        if (entity == null || entity.type == null) return null;

        if (entity.type.equals("text")) {
            TextNote textNote = new TextNote(entity.title, entity.content);
            textNote.setCreatedDate(entity.createdDate);
            return textNote;
        } else if (entity.type.equals("checklist")) {
            List<String> items = gson.fromJson(entity.checklistItemsJson, new TypeToken<List<String>>(){}.getType());
            CheckListNote checkListNote = new CheckListNote(entity.title, "");
            if (items != null) {
                checkListNote.setCheckList(items);
            }
            checkListNote.setCreatedDate(entity.createdDate);
            return checkListNote;
        }
        return null;
    }
}