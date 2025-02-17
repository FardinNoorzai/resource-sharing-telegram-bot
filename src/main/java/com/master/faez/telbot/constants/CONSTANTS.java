package com.master.faez.telbot.constants;

import java.util.List;

public class CONSTANTS {
    public final static String BACK = "Back";
    public final static String CANCEL = "Cancel";
    public final static List<String> KEYBOARD_HOME = List.of("Books","Broadcast Message","About us","Add new Admin","Delete an Admin");
    public final static List<String> ERROR_INVALID_KEYBOARD_KEY = List.of("Invalid keyboard key","Use the keyboard!");
    public final static List<String> KEYBOARD_CANCEL = List.of(CANCEL);
    public final static List<String> KEYBOARD_BOOK_MANAGEMENT = List.of("List Books","Create New Book","Back");
    public final static List<String> KEYBOARD_RESOURCE_MANAGEMENT = List.of("Delete book","Edit book","Create new Resource type","List Resources","Back");
    public final static List<String> KEYBOARD_FILE_MANAGEMENT = List.of("Delete Resource","Edit Resource","Add files","List files","Back");
    public final static List<String> KEYBOARD_FILE_SELECTED =List.of("Edit","Delete","Back");
}
