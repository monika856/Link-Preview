package com.example.monikasaini.preview.events;

import com.example.monikasaini.preview.models.PageData;

/**
 * Created by Monika on 06/06/17.
 */

public class EventDataFetched {

    private PageData data;

    public EventDataFetched(PageData data) {
        this.data = data;
    }

    public PageData getData() {
        return data;
    }
}
