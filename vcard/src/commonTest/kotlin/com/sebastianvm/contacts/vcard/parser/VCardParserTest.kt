package com.sebastianvm.contacts.vcard.parser

import com.sebastianvm.contacts.vcard.V2VCard
import com.sebastianvm.contacts.vcard.V3VCard
import com.sebastianvm.contacts.vcard.V4VCard
import com.sebastianvm.contacts.vcard.VCard
import com.sebastianvm.contacts.vcard.parameters.TypeParameter
import com.sebastianvm.contacts.vcard.properties.FormattedNameProperty
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

val VCardParserTest by testSuite {
    test("parses minimal v4 vcard") {
        val input =
            """
            BEGIN:VCARD
            VERSION:4.0
            FN:John Doe
            END:VCARD
            """
                .trimIndent()

        val result = VCard.parse(input)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        result.vcards.size shouldBe 1
        val vcard = result.vcards[0]
        vcard.shouldBeInstanceOf<V4VCard>()
        vcard.formattedName shouldBe FormattedNameProperty(value = "John Doe")
    }

    test("parses v4 vcard with all properties") {
        val input =
            """
            BEGIN:VCARD
            VERSION:4.0
            FN:John Doe
            N:Doe;John;Philip;Mr.;Jr.
            NICKNAME:Johnny
            EMAIL;TYPE=work:john@example.com
            EMAIL;TYPE=home;PREF=1:john@home.com
            TEL;TYPE=work,voice;VALUE=uri:tel:+1-555-555-5555
            ADR;TYPE=home:;;123 Main St;Anytown;CA;12345;USA
            ORG:Acme Corp;Engineering
            TITLE:Senior Engineer
            NOTE:A note about John
            URL:https://example.com
            BDAY:1990-01-15
            PHOTO:https://example.com/photo.jpg
            END:VCARD
            """
                .trimIndent()

        val result = VCard.parse(input)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        val vcard = result.vcards[0] as V4VCard

        vcard.formattedName.value shouldBe "John Doe"
        val n = vcard.name!!
        n.familyName shouldBe "Doe"
        n.givenName shouldBe "John"
        n.additionalNames shouldBe "Philip"
        n.honorificPrefixes shouldBe "Mr."
        n.honorificSuffixes shouldBe "Jr."
        vcard.nicknames.size shouldBe 1
        vcard.nicknames[0].value shouldBe listOf("Johnny")
        vcard.emailAddresses.size shouldBe 2
        vcard.emailAddresses[0].value shouldBe "john@example.com"
        vcard.emailAddresses[1].value shouldBe "john@home.com"
        vcard.telephoneNumbers.size shouldBe 1
        vcard.telephoneNumbers[0].value shouldBe "tel:+1-555-555-5555"
        vcard.physicalAddresses.size shouldBe 1
        vcard.physicalAddresses[0].street shouldBe "123 Main St"
        vcard.physicalAddresses[0].locality shouldBe "Anytown"
        vcard.physicalAddresses[0].country shouldBe "USA"
        vcard.organization!!.value shouldBe listOf("Acme Corp", "Engineering")
        vcard.title!!.value shouldBe "Senior Engineer"
        vcard.note!!.value shouldBe "A note about John"
        vcard.urls.size shouldBe 1
        vcard.urls[0].value shouldBe "https://example.com"
        vcard.birthday!!.value shouldBe "1990-01-15"
        vcard.photo!!.value shouldBe "https://example.com/photo.jpg"
    }

    test("parses v4 vcard with new RFC 6350 properties") {
        val input =
            """
            BEGIN:VCARD
            VERSION:4.0
            FN:Test User
            KIND:individual
            ANNIVERSARY:2010-06-15
            GENDER:M;male
            IMPP:xmpp:test@example.com
            LANG;PREF=1:en
            TZ:America/New_York
            GEO:geo:37.386013,-122.082932
            ROLE:Developer
            LOGO:https://example.com/logo.png
            MEMBER:urn:uuid:abc123
            RELATED;TYPE=spouse:urn:uuid:def456
            CATEGORIES:friend,colleague
            PRODID:-//Test//EN
            REV:20200101T000000Z
            SOUND:https://example.com/name.ogg
            UID:urn:uuid:f81d4fae
            CLIENTPIDMAP:1;urn:uuid:abc
            KEY:https://example.com/key.pub
            FBURL:https://example.com/fb
            CALADRURI:mailto:cal@example.com
            CALURI:https://example.com/cal
            SOURCE:ldap://ldap.example.com/o=Acme
            XML:<custom>data</custom>
            END:VCARD
            """
                .trimIndent()

        val result = VCard.parse(input)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        val vcard = result.vcards[0] as V4VCard

        vcard.kind!!.value shouldBe "individual"
        vcard.anniversary!!.value shouldBe "2010-06-15"
        val gender = vcard.gender!!
        gender.sex shouldBe "M"
        gender.text shouldBe "male"
        vcard.instantMessagingAddresses[0].value shouldBe "xmpp:test@example.com"
        vcard.spokenLanguages[0].value shouldBe "en"
        vcard.timezones[0].value shouldBe "America/New_York"
        vcard.geographicPositions[0].value shouldBe "geo:37.386013,-122.082932"
        vcard.roles[0].value shouldBe "Developer"
        vcard.logos[0].value shouldBe "https://example.com/logo.png"
        vcard.members[0].value shouldBe "urn:uuid:abc123"
        vcard.relatedPeople[0].value shouldBe "urn:uuid:def456"
        vcard.categories[0].value shouldBe listOf("friend", "colleague")
        vcard.productIdentifier!!.value shouldBe "-//Test//EN"
        vcard.revision!!.value shouldBe "20200101T000000Z"
        vcard.sounds[0].value shouldBe "https://example.com/name.ogg"
        vcard.uniqueIdentifier!!.value shouldBe "urn:uuid:f81d4fae"
        vcard.clientPidMaps[0].pidNumber shouldBe "1"
        vcard.clientPidMaps[0].uri shouldBe "urn:uuid:abc"
        vcard.keys[0].value shouldBe "https://example.com/key.pub"
        vcard.freeBusyUrls[0].value shouldBe "https://example.com/fb"
        vcard.calendarAddressUris[0].value shouldBe "mailto:cal@example.com"
        vcard.calendarUris[0].value shouldBe "https://example.com/cal"
        vcard.sources[0].value shouldBe "ldap://ldap.example.com/o=Acme"
        vcard.xmls[0].value shouldBe "<custom>data</custom>"
    }

    test("parses v3 vcard with shared properties") {
        val input =
            """
            BEGIN:VCARD
            VERSION:3.0
            FN:Jane Smith
            TZ:America/Chicago
            GEO:geo:40.7,-74.0
            ROLE:Manager
            CATEGORIES:business
            UID:urn:uuid:12345
            KEY:https://example.com/key
            END:VCARD
            """
                .trimIndent()

        val result = VCard.parse(input)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        val vcard = result.vcards[0] as V3VCard
        vcard.timezones[0].value shouldBe "America/Chicago"
        vcard.geographicPositions[0].value shouldBe "geo:40.7,-74.0"
        vcard.roles[0].value shouldBe "Manager"
        vcard.categories[0].value shouldBe listOf("business")
        vcard.uniqueIdentifier!!.value shouldBe "urn:uuid:12345"
        vcard.keys[0].value shouldBe "https://example.com/key"
    }

    test("returns failure for empty input") {
        val result = VCard.parse("")
        result.shouldBeInstanceOf<VCardParseResult.Failure>()
    }

    test("returns failure for input without BEGIN:VCARD") {
        val result = VCard.parse("just some text")
        result.shouldBeInstanceOf<VCardParseResult.Failure>()
    }

    test("parses multiple vcards") {
        val input =
            """
            BEGIN:VCARD
            VERSION:4.0
            FN:John Doe
            END:VCARD
            BEGIN:VCARD
            VERSION:4.0
            FN:Jane Smith
            END:VCARD
            """
                .trimIndent()

        val result = VCard.parse(input)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        result.vcards.size shouldBe 2
        (result.vcards[0] as V4VCard).formattedName.value shouldBe "John Doe"
        (result.vcards[1] as V4VCard).formattedName.value shouldBe "Jane Smith"
    }

    test("parses v2.1 vcard with bare type parameters") {
        val input =
            """
            BEGIN:VCARD
            VERSION:2.1
            N:Doe;John;;;
            FN:John Doe
            TEL;CELL:555-012-3456
            TEL;PAGER:+1-555-987-6543
            EMAIL;HOME:john@example.com
            EMAIL;WORK:john@work.example.com
            ADR;HOME:;;742 Evergreen Terrace;Springfield;IL;62704;US
            ORG:Acme Corp
            TITLE:Engineer
            NOTE:Some notes
            URL:https://example.com
            BDAY:1985-03-22
            PHOTO:https://example.com/photo.jpg
            NICKNAME:Johnny
            END:VCARD
            """
                .trimIndent()

        val result = VCard.parse(input)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        result.vcards.size shouldBe 1
        val vcard = result.vcards[0]
        vcard.shouldBeInstanceOf<V2VCard>()

        // Identification
        vcard.formattedName!!.value shouldBe "John Doe"
        val n = vcard.name!!
        n.familyName shouldBe "Doe"
        n.givenName shouldBe "John"
        n.additionalNames shouldBe ""
        n.honorificPrefixes shouldBe ""
        n.honorificSuffixes shouldBe ""
        vcard.nicknames.size shouldBe 1
        vcard.nicknames[0].value shouldBe listOf("Johnny")
        vcard.photo!!.value shouldBe "https://example.com/photo.jpg"
        vcard.birthday!!.value shouldBe "1985-03-22"

        // Delivery
        vcard.physicalAddresses.size shouldBe 1
        vcard.physicalAddresses[0].type shouldBe TypeParameter(listOf("HOME"))
        vcard.physicalAddresses[0].poBox shouldBe ""
        vcard.physicalAddresses[0].extendedAddress shouldBe ""
        vcard.physicalAddresses[0].street shouldBe "742 Evergreen Terrace"
        vcard.physicalAddresses[0].locality shouldBe "Springfield"
        vcard.physicalAddresses[0].region shouldBe "IL"
        vcard.physicalAddresses[0].postalCode shouldBe "62704"
        vcard.physicalAddresses[0].country shouldBe "US"

        // Communications
        vcard.telephoneNumbers.size shouldBe 2
        vcard.telephoneNumbers[0].value shouldBe "555-012-3456"
        vcard.telephoneNumbers[0].type shouldBe TypeParameter(listOf("CELL"))
        vcard.telephoneNumbers[1].value shouldBe "+1-555-987-6543"
        vcard.telephoneNumbers[1].type shouldBe TypeParameter(listOf("PAGER"))
        vcard.emailAddresses.size shouldBe 2
        vcard.emailAddresses[0].value shouldBe "john@example.com"
        vcard.emailAddresses[0].type shouldBe TypeParameter(listOf("HOME"))
        vcard.emailAddresses[1].value shouldBe "john@work.example.com"
        vcard.emailAddresses[1].type shouldBe TypeParameter(listOf("WORK"))

        // Organizational
        vcard.title!!.value shouldBe "Engineer"
        vcard.organization!!.value shouldBe listOf("Acme Corp")

        // Explanatory
        vcard.note!!.value shouldBe "Some notes"
        vcard.urls.size shouldBe 1
        vcard.urls[0].value shouldBe "https://example.com"

        // Properties not in input should be absent
        vcard.timezones shouldBe emptyList()
        vcard.geographicPositions shouldBe emptyList()
        vcard.roles shouldBe emptyList()
        vcard.logos shouldBe emptyList()
        vcard.revision shouldBe null
        vcard.sounds shouldBe emptyList()
        vcard.uniqueIdentifier shouldBe null
        vcard.keys shouldBe emptyList()
    }

    test("handles line unfolding during parsing") {
        val input = "BEGIN:VCARD\r\nVERSION:4.0\r\nFN:John\r\n  Doe\r\nEND:VCARD"

        val result = VCard.parse(input)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        (result.vcards[0] as V4VCard).formattedName.value shouldBe "John Doe"
    }

    test("handles line unfolding with mixed line endings") {
        val input = "BEGIN:VCARD\r\nVERSION:4.0\nFN:John\r\n  Doe\nEND:VCARD"

        val result = VCard.parse(input)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        (result.vcards[0] as V4VCard).formattedName.value shouldBe "John Doe"
    }

    test("handles line unfolding with multiple spaces and tabs") {
        val input = "BEGIN:VCARD\nVERSION:4.0\nFN:John\n  Doe\n\t Smith\nEND:VCARD"

        val result = VCard.parse(input)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        (result.vcards[0] as V4VCard).formattedName.value shouldBe "John Doe Smith"
    }

    test("parses escaped characters in simple values") {
        val input =
            """
            BEGIN:VCARD
            VERSION:4.0
            FN:John\;Doe\, Esq.
            NOTE:This is a note\\with a newline\nand a comma\, and semicolon\;.
            END:VCARD
            """
                .trimIndent()

        val result = VCard.parse(input)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        val vcard = result.vcards[0] as V4VCard
        vcard.formattedName.value shouldBe "John;Doe, Esq."
        vcard.note!!.value shouldBe "This is a note\\with a newline\nand a comma, and semicolon;."
    }

    test("parses escaped characters in structured values") {
        val input =
            """
            BEGIN:VCARD
            VERSION:4.0
            FN:John Doe
            N:Doe\;Surname;John\;Given;Middle\;Name;;
            ADR;TYPE=home:;;123 Main\; St;City\, State;ST;12345;US
            END:VCARD
            """
                .trimIndent()

        val result = VCard.parse(input)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        val vcard = result.vcards[0] as V4VCard
        vcard.name!!.familyName shouldBe "Doe;Surname"
        vcard.name.givenName shouldBe "John;Given"
        vcard.name.additionalNames shouldBe "Middle;Name"
        vcard.physicalAddresses[0].street shouldBe "123 Main; St"
        vcard.physicalAddresses[0].locality shouldBe "City, State"
    }

    test("parses escaped characters in list values") {
        val input =
            """
            BEGIN:VCARD
            VERSION:4.0
            FN:John Doe
            CATEGORIES:Category\, One,Category\;Two,Category\\Three
            END:VCARD
            """
                .trimIndent()

        val result = VCard.parse(input)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        val vcard = result.vcards[0] as V4VCard
        vcard.categories[0].value shouldBe
            listOf("Category, One", "Category;Two", "Category\\Three")
    }
}
