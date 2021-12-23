package ru.pshiblo.elgamal.gui.views

import com.google.gson.Gson
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.control.Alert
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import ru.pshiblo.elgamal.core.crypto.key.GeneratorKey
import ru.pshiblo.elgamal.core.crypto.key.SecretKey
import ru.pshiblo.elgamal.core.crypto.key.base.BigKey
import ru.pshiblo.elgamal.core.crypto.service.DecryptorService
import ru.pshiblo.elgamal.core.crypto.service.GamalCryptoResult
import ru.pshiblo.elgamal.gui.fragments.GenerateNewKeySetUpFragment
import ru.pshiblo.elgamal.gui.properties.SecretKeyModel
import tornadofx.*


class RecipientView : View("Получатель") {

    var secretKey: SecretKey? = null
    val secretKeyModel = SecretKeyModel()
    val msgProperty = SimpleStringProperty()
    val decryptProperty = SimpleStringProperty()

    private val decryptorService: DecryptorService by di()

    override val root = borderpane {
        style {
            padding = box(20.px)
        }
        top {
            form {
                fieldset("Ключ", labelPosition = Orientation.VERTICAL) {
                    field("Размер ключа в битах") {
                        textfield(secretKeyModel.sizeBitsProperty) {
                            isEditable = false
                        }
                    }
                    field("Размер ключа в байтах") {
                        textfield(secretKeyModel.sizeByteProperty) {
                            isEditable = false
                        }
                    }
                    field("X") {
                        textfield(secretKeyModel.xProperty) {
                            isEditable = false
                        }
                    }
                    field("P") {
                        textfield(secretKeyModel.pProperty) {
                            isEditable = false
                        }
                    }
                    field("G") {
                        textfield(secretKeyModel.gProperty) {
                            isEditable = false
                        }
                    }
                    field("Y") {
                        textfield(secretKeyModel.yProperty) {
                            isEditable = false
                        }
                    }
                    buttonbar {
                        button("Загрузить закрытый ключ") {
                            action {
                                val filesSaved = chooseFile(
                                    "Открыть закрытый ключ",
                                    arrayOf(FileChooser.ExtensionFilter("File", "*.private")),
                                )
                                if (filesSaved.isNotEmpty()) {
                                    val fileSaved = filesSaved[0]
                                    if (fileSaved.exists()) {
                                        val allText = fileSaved.readText()
                                        val listElements = allText.split("\n")
                                        if (listElements.size != 4) {
                                            alert(Alert.AlertType.ERROR, "Ошибка", "Публичный ключ не валидный!")
                                            return@action
                                        }
                                        secretKey = SecretKey(
                                            BigKey(listElements[0].substring(3)),
                                            BigKey(listElements[1].substring(3)),
                                            BigKey(listElements[2].substring(3)),
                                            BigKey(listElements[3].substring(3))
                                        )
                                        secretKeyModel.setPropertiesSecretKey(secretKey!!)
                                    }
                                }
                            }
                        }
                        button("Сгенерировать новые ключи") {
                            action {
                                find<GenerateNewKeySetUpFragment>().openModal()
                            }
                        }
                        button("Сохранить открытый ключ в файл") {
                            action {
                                if (secretKey != null) {
                                    val filesSaved = chooseFile(
                                        "Сохранить открытый ключ",
                                        arrayOf(FileChooser.ExtensionFilter("File", "*.pub")),
                                        mode = FileChooserMode.Save
                                    )
                                    if (filesSaved.isNotEmpty()) {
                                        val fileSaved = filesSaved[0]
                                        val strKey = "p. ${secretKey?.p.toString()}\n" +
                                                "g. ${secretKey?.g.toString()}\n" +
                                                "y. ${secretKey?.y.toString()}"
                                        if (fileSaved.exists()) {
                                            fileSaved.delete()
                                        }
                                        fileSaved.createNewFile()
                                        fileSaved.writeText(strKey)
                                    }
                                }
                            }
                        }
                        button("Сохранить закрытый ключ в файл") {
                            action {
                                if (secretKey != null) {
                                    val filesSaved = chooseFile(
                                        "Сохранить закрытый ключ",
                                        arrayOf(FileChooser.ExtensionFilter("File", "*.private")),
                                        mode = FileChooserMode.Save
                                    )
                                    if (filesSaved.isNotEmpty()) {
                                        val fileSaved = filesSaved[0]
                                        val strKey = "p. ${secretKey?.p.toString()}\n" +
                                                "g. ${secretKey?.g.toString()}\n" +
                                                "y. ${secretKey?.y.toString()}\n" +
                                                "x. ${secretKey?.x.toString()}"
                                        if (fileSaved.exists()) {
                                            fileSaved.delete()
                                        }
                                        fileSaved.createNewFile()
                                        fileSaved.writeText(strKey)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        center {
            separator(orientation = Orientation.HORIZONTAL)
            form {
                fieldset("Расшифровать сообщение", labelPosition = Orientation.VERTICAL) {
                    field("Зашифрованное сообщение") {
                        textarea(msgProperty) {
                            prefRowCount = 5
                            vgrow = Priority.ALWAYS
                        }
                    }
                    button("Загрузить зашифрованное сообщение из файла...") {
                        action {
                            val filesSaved = chooseFile(
                                "Открыть закрытый ключ",
                                arrayOf(FileChooser.ExtensionFilter("File", "*.txt")),
                            )
                            if (filesSaved.isNotEmpty()) {
                                val fileSaved = filesSaved[0]
                                if (fileSaved.exists()) {
                                    val allText = fileSaved.readText()
                                    msgProperty.value = allText
                                }
                            }
                        }
                    }
                    button("Расшифровать") {
                        action {
                            val gson = Gson()
                            val gamalCryptoResult = gson.fromJson(msgProperty.value, GamalCryptoResult::class.java)
                            decryptProperty.value = decryptorService.decryption(gamalCryptoResult, secretKey)
                        }
                    }
                    field("Расшифрованное сообщение") {
                        textarea(decryptProperty) {
                            prefRowCount = 5
                            vgrow = Priority.ALWAYS
                        }
                    }
                }
            }
        }
    }
}
