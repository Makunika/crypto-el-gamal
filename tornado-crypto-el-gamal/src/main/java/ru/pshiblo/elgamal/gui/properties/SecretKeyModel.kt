package ru.pshiblo.elgamal.gui.properties

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import ru.pshiblo.elgamal.core.crypto.key.SecretKey

class SecretKeyModel {
    var sizeBitsProperty = SimpleIntegerProperty(this, "Size", 0)
    var sizeBits by sizeBitsProperty
    var sizeByteProperty = SimpleIntegerProperty(this, "Size", 0)
    var sizeByte by sizeByteProperty
    var xProperty = SimpleStringProperty(this, "X", "")
    var x by xProperty
    var yProperty = SimpleStringProperty(this, "Y", "")
    var y by yProperty
    var gProperty = SimpleStringProperty(this, "G", "")
    var g by gProperty
    var pProperty = SimpleStringProperty(this, "P", "")
    var p by pProperty

    fun setPropertiesSecretKey(secretKey: SecretKey) {
        x = secretKey.x.toString()
        y = secretKey.y.toString()
        g = secretKey.g.toString()
        p = secretKey.p.toString()
        sizeBits = secretKey.p.bitsSize
        sizeByte = secretKey.p.byteSize
    }
}