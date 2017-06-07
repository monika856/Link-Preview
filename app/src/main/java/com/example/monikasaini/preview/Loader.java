package com.example.monikasaini.preview;

/**
 * Created by Monika on 06/06/17.
 */

public interface Loader {

    int PROGRESS_DIALOG = 1;

    void showLoader();

    void dismissLoader(Runnable run);
}
