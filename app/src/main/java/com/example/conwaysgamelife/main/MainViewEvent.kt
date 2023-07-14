package com.example.conwaysgamelife.main

sealed interface MainViewEvent {

    object OnStopClick : MainViewEvent
    object OnPlayClick : MainViewEvent
    object OnRandomClick : MainViewEvent
}