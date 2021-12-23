package ru.pshiblo.elgamal.gui.views

import tornadofx.*

class MainView : View("My View") {
    override val root = tabpane {
        tab<SenderView>()
        tab<RecipientView>()
    }
}
