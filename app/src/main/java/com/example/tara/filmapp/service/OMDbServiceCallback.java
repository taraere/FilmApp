package com.example.tara.filmapp.service;

import com.example.tara.filmapp.data.Film;

/**
 * Created by Tara on 12/12/2017.
 */

public interface OMDbServiceCallback {
    void serviceSuccess(Film film);
    void serviceFailure(Exception exception);
}
