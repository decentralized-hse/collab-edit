package com.github.servb.collabEdit.intTest

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

    fun loginAs(userName: String): CallPage {
        driver.`$`("#usernameInput").sendKeys(userName)
        driver.`$`("#loginBtn").click()
        return driver.page(CallPage(driver, userName))
    }
}

class CallPage(val driver: SelenideDriver, val userName: String) {

    fun call(userName: String) {
        driver.`$`("#callToUsernameInput").sendKeys(userName)
        driver.`$`("#callBtn").click()
    }

    fun input(message: String) {
        driver.`$`("#text").sendKeys(message)
    }

    val text: SelenideElement get() = driver.`$`("#text")
}
