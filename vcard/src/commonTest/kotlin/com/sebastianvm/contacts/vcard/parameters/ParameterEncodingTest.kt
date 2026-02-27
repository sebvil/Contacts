package com.sebastianvm.contacts.vcard.parameters

import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

val ParameterEncodingTest by testSuite {
    test("SORT-AS encodes as quoted comma-separated") {
        SortAsParameter(listOf("Doe", "John")).toVCardString() shouldBe """SORT-AS="Doe,John""""
    }

    test("SORT-AS single value") {
        SortAsParameter(listOf("Doe")).toVCardString() shouldBe """SORT-AS="Doe""""
    }

    test("GEO parameter encodes quoted URI") {
        GeoParameter("geo:37.386013,-122.082932").toVCardString() shouldBe
            """GEO="geo:37.386013,-122.082932""""
    }

    test("TZ parameter encodes quoted value") {
        TzParameter("America/New_York").toVCardString() shouldBe """TZ="America/New_York""""
    }

    test("TZ parameter with UTC offset") {
        TzParameter("-05:00").toVCardString() shouldBe """TZ="-05:00""""
    }

    test("LABEL parameter encodes value") {
        LabelParameter("123 Main St").toVCardString() shouldBe "LABEL=123 Main St"
    }

    test("MEDIATYPE encodes value") {
        MediatypeParameter("image/jpeg").toVCardString() shouldBe "MEDIATYPE=image/jpeg"
    }

    test("CALSCALE encodes value") {
        CalscaleParameter("gregorian").toVCardString() shouldBe "CALSCALE=gregorian"
    }

    test("ALTID encodes value") { AltIdParameter("1").toVCardString() shouldBe "ALTID=1" }

    test("PID encodes value") { PidParameter("1.1").toVCardString() shouldBe "PID=1.1" }
}
