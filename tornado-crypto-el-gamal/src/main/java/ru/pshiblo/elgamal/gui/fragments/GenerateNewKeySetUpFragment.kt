package ru.pshiblo.elgamal.gui.fragments

import javafx.beans.property.SimpleIntegerProperty
import ru.pshiblo.elgamal.core.crypto.key.GeneratorKey
import ru.pshiblo.elgamal.gui.views.RecipientView
import tornadofx.*

class GenerateNewKeySetUpFragment: Fragment("Сгенерировать новые ключи") {

    private var sizeBitsProperty = SimpleIntegerProperty(1024)
    private val generatorKey: GeneratorKey by di()
    private val recipientView: RecipientView by inject()

    override val root = borderpane {
        style {
            padding = box(20.px)
        }
        top {
            form {
                fieldset("Сгенерировать новый ключ") {
                    field("Размер ключа в битах") {
                        textfield(sizeBitsProperty)
                    }
                }
                buttonbar {
                    button("Сгенерировать ключи") {
                        action {
                            val newKey = generatorKey.generateNewKey(sizeBitsProperty.value)
                            recipientView.secretKey = newKey
                            recipientView.secretKeyModel.setPropertiesSecretKey(newKey)
                            close()
                        }
                    }
                }
            }
        }
    }
}