package ru.pshiblo.elgamal.gui

import javafx.stage.Stage
import ru.pshiblo.elgamal.gui.views.MainView
import tornadofx.*

class MyApp: App(MainView::class) {
    override fun start(stage: Stage) {
        stage.centerOnScreen()
        super.start(stage)
    }
}