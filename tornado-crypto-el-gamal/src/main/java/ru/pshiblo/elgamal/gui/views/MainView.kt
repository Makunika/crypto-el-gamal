package ru.pshiblo.elgamal.gui.views

import tornadofx.*

class MainView : View("My View") {
    override val root = vbox {
        style {
            padding = box(100.px)
        }
        label("дратути")
    }
}
