package ru.pshiblo.elgamal.gui

import com.google.inject.Guice
import javafx.stage.Stage
import ru.pshiblo.elgamal.core.CryptoModule
import ru.pshiblo.elgamal.gui.views.MainView
import tornadofx.*
import kotlin.reflect.KClass

class MyApp: App(MainView::class) {

    init {
        val guice = Guice.createInjector(CryptoModule())
        FX.dicontainer = object : DIContainer {
            override fun <T : Any> getInstance(type: KClass<T>)
                    = guice.getInstance(type.java)
        }
    }

    override fun start(stage: Stage) {
        stage.centerOnScreen()
        super.start(stage)
    }
}