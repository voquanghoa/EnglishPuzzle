package com.qhvv.englishpuzzle.configuration;

/**
 * Created by voqua on 11/1/2015.
 */
public interface AppConstants {

    String PACKAGE_FOLDER = "packages";
    String ASSERT_DATA_FOLDER = "data";
    String ASSERT_JSON_FILE = ASSERT_DATA_FOLDER+"/%s/list.json";

    String ZIP_EXTENSION = ".zip";
    String PNG_EXTENSION = ".png";
    String AUDIO_EXETENSION = ".mp3";
    String KEYBOARD_IMAGES_PATH = "keyboard.images/";
    String KEYBOARD_AUDIOS_PATH = "keyboard.sounds/";

    /*String[] KEYBOARD_LAYOUT = new String[]{
            "qwertyuiop",
            "asdfghjkl",
            "zxcvbnm"
    };*/
    String[] KEYBOARD_LAYOUT = new String[]{
            "abcdefg",
            "hijklmn",
            "opqrstu",
            "vwxyz"
    };
    String BACKGROUND_KEYBOARD = "/__background_kb";
    String BACKGROUND_MAIN = "/__background_main";
    String BACKGROUND_CATEGORY = "/__background";

    String CATEGORY_MESSAGE = "category";
    String DATA_CATEGORIES_JSON_PATH = "data/categories.json";
    int ANIMATION_DURATION = 3000;

    String DONE_FILE_NAME = "done";
    String COVER_FILE_NAME = "__cover";

    String ASSERT_AUDIO_FILE_PATH = ASSERT_DATA_FOLDER + "/%s"+AUDIO_EXETENSION;
    String ASSERT_BITMAP_FILE_PATH = ASSERT_DATA_FOLDER + "/%s"+PNG_EXTENSION;

    String STORAGE_AUDIO_FILE_PATH = PACKAGE_FOLDER + "/%s"+AUDIO_EXETENSION;
    String STORAGE_BITMAP_FILE_PATH = PACKAGE_FOLDER + "/%s"+PNG_EXTENSION;
}
