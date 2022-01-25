package ru.pshiblo.elgamal.gui.views

import com.google.gson.Gson
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.control.Alert
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import ru.pshiblo.elgamal.core.crypto.key.OpenKey
import ru.pshiblo.elgamal.core.crypto.key.base.BigKey
import ru.pshiblo.elgamal.core.crypto.service.CryptorService
import ru.pshiblo.elgamal.gui.properties.OpenKeyModel
import tornadofx.*

class SenderView : View("Отправитель") {
    val openKeyModelModel = OpenKeyModel()
    val msgProperty = SimpleStringProperty()
    val bitsProperty = SimpleIntegerProperty(1008)
    val encryptProperty = SimpleStringProperty()

    private val cryptorService: CryptorService by di()


    override val root = borderpane {
        style {
            padding = box(20.px)
        }
        top {
            form {
                fieldset("Ключ", labelPosition = Orientation.VERTICAL) {
                    field("P") {
                        textfield(openKeyModelModel.pProperty)
                    }
                    field("G") {
                        textfield(openKeyModelModel.gProperty)
                    }
                    field("Y") {
                        textfield(openKeyModelModel.yProperty)
                    }
                    buttonbar {
                        button("Загрузить открытый ключ из файла") {
                            action {
                                val filesSaved = chooseFile(
                                    "Открыть открытый ключ",
                                    arrayOf(FileChooser.ExtensionFilter("File", "*.pub")),
                                )
                                if (filesSaved.isNotEmpty()) {
                                    val fileSaved = filesSaved[0]
                                    val allText = fileSaved.readText()
                                    val listElements = allText.split("\n")
                                    if (listElements.size != 3) {
                                        alert(Alert.AlertType.ERROR, "Ошибка", "Публичный ключ не валидный!")
                                        return@action
                                    }
                                    openKeyModelModel.pProperty.value = listElements[0].substring(3)
                                    openKeyModelModel.gProperty.value = listElements[1].substring(3)
                                    openKeyModelModel.yProperty.value = listElements[2].substring(3)
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
                fieldset("Сообщение", labelPosition = Orientation.VERTICAL) {
                    field("Сообщение") {
                        textarea(msgProperty) {
                            prefRowCount = 5
                            vgrow = Priority.ALWAYS
                        }
                    }
                    field("Размер блока шифрования в битах (>1)") {
                        textfield(bitsProperty)
                    }
                    button("Зашифровать") {
                        action {
                            if (openKeyModelModel.pProperty.value.isNullOrEmpty()) {
                                alert(Alert.AlertType.ERROR, "Ошибка", "Компонент P не может быть пустым")
                                return@action
                            }
                            if (openKeyModelModel.gProperty.value.isNullOrEmpty()) {
                                alert(Alert.AlertType.ERROR, "Ошибка", "Компонент G не может быть пустым")
                                return@action
                            }
                            if (openKeyModelModel.yProperty.value.isNullOrEmpty()) {
                                alert(Alert.AlertType.ERROR, "Ошибка", "Компонент Y не может быть пустым")
                                return@action
                            }
                            if (msgProperty.value.isNullOrEmpty()) {
                                alert(Alert.AlertType.ERROR, "Ошибка", "Нет текста для зашифрования!")
                                return@action
                            }
                            if (bitsProperty.value <= 1) {
                                alert(Alert.AlertType.ERROR, "Ошибка", "Размер блока в битах должно быть больше 1")
                                return@action
                            }
                            val openKey = OpenKey(BigKey(openKeyModelModel.pProperty.value), BigKey(openKeyModelModel.gProperty.value), BigKey(openKeyModelModel.yProperty.value))
                            if (openKey.p.bitsSize <= bitsProperty.value) {
                                alert(Alert.AlertType.ERROR, "Ошибка", "Размер блока для шифрования должен быть меньше, чем размер ключа (${openKey.p.bitsSize})")
                                return@action
                            }
                            val gson = Gson()
                            val gamalCryptoResult = cryptorService.encryption(msgProperty.value, openKey, bitsProperty.value / 8)
                            val jsonGamalResult = gson.toJson(gamalCryptoResult)
                            encryptProperty.value = jsonGamalResult
                        }
                    }
                    field("Зашифрованное сообщение") {
                        textarea(encryptProperty) {
                            prefRowCount = 5
                            vgrow = Priority.ALWAYS
                        }
                    }
                    button("Сохранить зашифрованное сообщение в файл...") {
                        action {
                            val filesSaved = chooseFile(
                                "Сохранить зашифрованное сообщение",
                                arrayOf(FileChooser.ExtensionFilter("File", "*.txt")),
                                mode = FileChooserMode.Save
                            )
                            if (filesSaved.isNotEmpty()) {
                                val fileSaved = filesSaved[0]
                                if (fileSaved.exists()) {
                                    fileSaved.delete()
                                }
                                fileSaved.createNewFile()
                                fileSaved.writeText(encryptProperty.value)
                            }
                        }
                    }
                }
            }
        }
    }
}
