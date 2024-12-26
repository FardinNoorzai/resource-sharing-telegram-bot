package com.master.faez.telbot.constants;

import java.util.List;

public class CONSTANTS {
    public final static String BOOKS = "Books";
    public final static String BACK = "Back";
    public final static String CREATE_NEW_BOOK = "Create new Book";
    public final static String LIST_BOOKS = "List all Books";
    public final static String CANCEL = "Cancel";
    public final static List<String> KEYBOARD_BOOK = List.of(CREATE_NEW_BOOK, LIST_BOOKS,BACK);
    public final static List<String> KEYBOARD_HOME = List.of(BOOKS);
    public final static List<String> ERROR_INVALID_KEYBOARD_KEY = List.of("Invalid keyboard key","Use the keyboard!");
    public final static List<String> KEYBOARD_CANCEL = List.of(CANCEL);


}
