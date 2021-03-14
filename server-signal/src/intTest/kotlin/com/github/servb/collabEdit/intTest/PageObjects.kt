package com.github.servb.collabEdit.intTest

import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.SelenideDriver
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.impl.StaticConfig

private val config by lazy { StaticConfig() }

fun openClient(): LoginPage {
    val driver = SelenideDriver(config)
    driver.open(ConnectionUtil.clientUrl)
    return driver.page(LoginPage(driver))
}

class LoginPage(val driver: SelenideDriver) {

    fun loginAs(userName: String): ConnectionPage {
        driver.`$`("#usernameInput").sendKeys(userName)
        driver.`$`("#loginBtn").click()
        return driver.page(ConnectionPage(driver, userName))
    }
}

class ConnectionPage(val driver: SelenideDriver, val userName: String) {

    fun connect(otherUserName: String): CollaborationPage {
        driver.`$`("#connectToUsernameInput").sendKeys(otherUserName)
        driver.`$`("#connectBtn").click()
        return CollaborationPage(driver, userName = userName, otherUserName = otherUserName)
    }

    infix fun shouldBeConnectedTo(otherUserName: String): CollaborationPage {
        driver.`$`("#text").should(exist)
        return CollaborationPage(driver, userName = userName, otherUserName = otherUserName)
    }
}

class CollaborationPage(val driver: SelenideDriver, val userName: String, val otherUserName: String) {

    fun input(message: CharSequence) {
        driver.`$`("#text").sendKeys(message)
    }

    fun disconnect(): ConnectionPage {
        driver.`$`("#disconnectBtn").click()
        return ConnectionPage(driver, userName)
    }

    val text: SelenideElement get() = driver.`$`("#text")
}
