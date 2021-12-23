package ru.pshiblo.elgamal.gui.properties

import javafx.beans.property.SimpleStringProperty
import ru.pshiblo.elgamal.core.crypto.key.OpenKey
import tornadofx.*

class OpenKeyModel {
    var yProperty = SimpleStringProperty(this, "Y", "")
    var y by yProperty
    var gProperty = SimpleStringProperty(this, "G", "")
    var g by gProperty
    var pProperty = SimpleStringProperty(this, "P", "")
    var p by pProperty


    fun setPropertiesOpenKey(openKey: OpenKey) {
        y = openKey.y.toString()
        g = openKey.g.toString()
        p = openKey.p.toString()
    }
}