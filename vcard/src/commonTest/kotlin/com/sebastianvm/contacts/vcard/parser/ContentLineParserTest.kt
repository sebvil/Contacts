package com.sebastianvm.contacts.vcard.parser

import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

val ContentLineParserTest by testSuite {
    test("parses simple property") {
        val result = ContentLineParser.parse("FN:John Doe")
        result.name shouldBe "FN"
        result.value shouldBe "John Doe"
        result.parameters shouldBe emptyList()
        result.group shouldBe null
    }

    test("parses property with parameter") {
        val result = ContentLineParser.parse("FN;LANGUAGE=en:John Doe")
        result.name shouldBe "FN"
        result.value shouldBe "John Doe"
        result.parameters.size shouldBe 1
        result.parameters[0].name shouldBe "LANGUAGE"
        result.parameters[0].value shouldBe "en"
    }

    test("parses property with multiple parameters") {
        val result = ContentLineParser.parse("EMAIL;TYPE=work;PREF=1:john@example.com")
        result.name shouldBe "EMAIL"
        result.value shouldBe "john@example.com"
        result.parameters.size shouldBe 2
        result.parameters[0].name shouldBe "TYPE"
        result.parameters[0].value shouldBe "work"
        result.parameters[1].name shouldBe "PREF"
        result.parameters[1].value shouldBe "1"
    }

    test("parses property with quoted parameter value") {
        val result = ContentLineParser.parse("""ADR;TYPE="work":;;123 Main St""")
        result.name shouldBe "ADR"
        result.parameters[0].name shouldBe "TYPE"
        result.parameters[0].value shouldBe "work"
    }

    test("parses property with group") {
        val result = ContentLineParser.parse("item1.TEL:+1-555-555-5555")
        result.group shouldBe "item1"
        result.name shouldBe "TEL"
        result.value shouldBe "+1-555-555-5555"
    }

    test("parses property with multi-value type parameter") {
        val result = ContentLineParser.parse("TEL;TYPE=work,voice:tel:+1-555")
        result.name shouldBe "TEL"
        result.parameters[0].name shouldBe "TYPE"
        result.parameters[0].value shouldBe "work,voice"
        result.value shouldBe "tel:+1-555"
    }

    test("parses structured value") {
        val result = ContentLineParser.parse("N:Doe;John;Philip;Mr.;Jr.")
        result.name shouldBe "N"
        result.value shouldBe "Doe;John;Philip;Mr.;Jr."
    }

    test("normalizes property name to uppercase") {
        val result = ContentLineParser.parse("fn:John Doe")
        result.name shouldBe "FN"
    }

    test("parses VERSION property") {
        val result = ContentLineParser.parse("VERSION:4.0")
        result.name shouldBe "VERSION"
        result.value shouldBe "4.0"
    }
}
