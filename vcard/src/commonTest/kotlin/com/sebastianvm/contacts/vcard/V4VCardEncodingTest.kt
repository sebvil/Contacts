package com.sebastianvm.contacts.vcard

import com.sebastianvm.contacts.vcard.parameters.LanguageParameter
import com.sebastianvm.contacts.vcard.properties.FnProperty
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

val V4VCardEncodingTest by testSuite {

    test("encodes a v4 vcard") {
        val vcard = V4VCard(fn = FnProperty(value = "John Doe", language = LanguageParameter("en")))
        val expected = """
            BEGIN:VCARD
            VERSION:4.0
            FN;LANGUAGE=en:John Doe
            END:VCARD
        """.trimIndent()
        vcard.toVCardString() shouldBe expected
    }
}