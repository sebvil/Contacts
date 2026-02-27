package com.sebastianvm.contacts.vcard.parser

import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

val LineUnfolderTest by testSuite {
    test("handles simple lines") {
        val input = "FN:John Doe\nEMAIL:john@example.com"
        val result = LineUnfolder.unfold(input)
        result shouldBe listOf("FN:John Doe", "EMAIL:john@example.com")
    }

    test("unfolds continuation lines with space") {
        val input = "NOTE:This is a long\n note that continues"
        val result = LineUnfolder.unfold(input)
        result shouldBe listOf("NOTE:This is a longnote that continues")
    }

    test("unfolds continuation lines with tab") {
        val input = "NOTE:This is a long\n\tnote that continues"
        val result = LineUnfolder.unfold(input)
        result shouldBe listOf("NOTE:This is a longnote that continues")
    }

    test("handles CRLF line endings") {
        val input = "FN:John Doe\r\nEMAIL:john@example.com"
        val result = LineUnfolder.unfold(input)
        result shouldBe listOf("FN:John Doe", "EMAIL:john@example.com")
    }

    test("handles CRLF with continuation") {
        val input = "NOTE:Long line\r\n continues here"
        val result = LineUnfolder.unfold(input)
        result shouldBe listOf("NOTE:Long linecontinues here")
    }

    test("filters blank lines") {
        val input = "FN:John Doe\n\nEMAIL:john@example.com"
        val result = LineUnfolder.unfold(input)
        result shouldBe listOf("FN:John Doe", "EMAIL:john@example.com")
    }

    test("handles multiple continuation lines") {
        val input = "NOTE:Line one\n continues\n more"
        val result = LineUnfolder.unfold(input)
        result shouldBe listOf("NOTE:Line onecontinuesmore")
    }
}
