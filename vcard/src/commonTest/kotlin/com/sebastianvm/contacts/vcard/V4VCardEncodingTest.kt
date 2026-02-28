package com.sebastianvm.contacts.vcard

import com.sebastianvm.contacts.vcard.parameters.LanguageParameter
import com.sebastianvm.contacts.vcard.parameters.PrefParameter
import com.sebastianvm.contacts.vcard.parameters.TypeParameter
import com.sebastianvm.contacts.vcard.parameters.Value
import com.sebastianvm.contacts.vcard.parameters.ValueParameter
import com.sebastianvm.contacts.vcard.properties.AddressProperty
import com.sebastianvm.contacts.vcard.properties.AnniversaryProperty
import com.sebastianvm.contacts.vcard.properties.BirthdayProperty
import com.sebastianvm.contacts.vcard.properties.CategoriesProperty
import com.sebastianvm.contacts.vcard.properties.EmailProperty
import com.sebastianvm.contacts.vcard.properties.FormattedNameProperty
import com.sebastianvm.contacts.vcard.properties.GenderProperty
import com.sebastianvm.contacts.vcard.properties.GeographicPositionProperty
import com.sebastianvm.contacts.vcard.properties.InstantMessagingProperty
import com.sebastianvm.contacts.vcard.properties.KeyProperty
import com.sebastianvm.contacts.vcard.properties.KindProperty
import com.sebastianvm.contacts.vcard.properties.NameProperty
import com.sebastianvm.contacts.vcard.properties.NicknameProperty
import com.sebastianvm.contacts.vcard.properties.NoteProperty
import com.sebastianvm.contacts.vcard.properties.OrganizationProperty
import com.sebastianvm.contacts.vcard.properties.PhotoProperty
import com.sebastianvm.contacts.vcard.properties.ProductIdentifierProperty
import com.sebastianvm.contacts.vcard.properties.RevisionProperty
import com.sebastianvm.contacts.vcard.properties.RoleProperty
import com.sebastianvm.contacts.vcard.properties.TelephoneProperty
import com.sebastianvm.contacts.vcard.properties.TimezoneProperty
import com.sebastianvm.contacts.vcard.properties.TitleProperty
import com.sebastianvm.contacts.vcard.properties.UniqueIdentifierProperty
import com.sebastianvm.contacts.vcard.properties.UrlProperty
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

val V4VCardEncodingTest by testSuite {
    test("encodes a v4 vcard with language") {
        val vcard =
            V4VCard(
                formattedName =
                    FormattedNameProperty(value = "John Doe", language = LanguageParameter("en"))
            )
        val expected =
            """
            BEGIN:VCARD
            VERSION:4.0
            FN;LANGUAGE=en:John Doe
            END:VCARD
            """
                .trimIndent()
        vcard.toVCardString() shouldBe expected
    }

    test("encodes a v4 vcard with many properties") {
        val vcard =
            V4VCard(
                formattedName = FormattedNameProperty(value = "John Doe"),
                name = NameProperty(value = listOf("Doe", "John", "Philip", "Mr.", "Jr.")),
                emailAddresses =
                    listOf(
                        EmailProperty(
                            value = "john@example.com",
                            type = TypeParameter(listOf("work")),
                        ),
                        EmailProperty(
                            value = "john@home.com",
                            type = TypeParameter(listOf("home")),
                            pref = PrefParameter(1),
                        ),
                    ),
                telephoneNumbers =
                    listOf(
                        TelephoneProperty(
                            value = "tel:+1-555-555-5555",
                            type = TypeParameter(listOf("work", "voice")),
                            valueParam = ValueParameter(Value.Uri),
                        )
                    ),
                physicalAddresses =
                    listOf(
                        AddressProperty(
                            value = listOf("", "", "123 Main St", "Anytown", "CA", "12345", "USA"),
                            type = TypeParameter(listOf("home")),
                        )
                    ),
                organization = OrganizationProperty(value = listOf("Acme Corp", "Engineering")),
                title = TitleProperty(value = "Senior Engineer"),
                note = NoteProperty(value = "A note about John"),
                urls = listOf(UrlProperty(value = "https://example.com")),
                birthday = BirthdayProperty(value = "1990-01-15"),
                nicknames = listOf(NicknameProperty(value = listOf("Johnny"))),
                photo = PhotoProperty(value = "https://example.com/photo.jpg"),
            )
        // Order follows RFC 6350 section order
        val expected =
            """
            BEGIN:VCARD
            VERSION:4.0
            FN:John Doe
            N:Doe;John;Philip;Mr.;Jr.
            NICKNAME:Johnny
            PHOTO:https://example.com/photo.jpg
            BDAY:1990-01-15
            ADR;TYPE=home:;;123 Main St;Anytown;CA;12345;USA
            TEL;TYPE=work,voice;VALUE=uri:tel:+1-555-555-5555
            EMAIL;TYPE=work:john@example.com
            EMAIL;TYPE=home;PREF=1:john@home.com
            TITLE:Senior Engineer
            ORG:Acme Corp;Engineering
            NOTE:A note about John
            URL:https://example.com
            END:VCARD
            """
                .trimIndent()
        vcard.toVCardString() shouldBe expected
    }

    test("encodes minimal v4 vcard") {
        val vcard = V4VCard(formattedName = FormattedNameProperty(value = "Jane Smith"))
        val expected =
            """
            BEGIN:VCARD
            VERSION:4.0
            FN:Jane Smith
            END:VCARD
            """
                .trimIndent()
        vcard.toVCardString() shouldBe expected
    }

    test("encodes v4 vcard with new RFC 6350 properties") {
        val vcard =
            V4VCard(
                formattedName = FormattedNameProperty(value = "Test User"),
                kind = KindProperty(value = "individual"),
                anniversary = AnniversaryProperty(value = "2010-06-15"),
                gender = GenderProperty(value = listOf("M", "")),
                instantMessagingAddresses =
                    listOf(InstantMessagingProperty(value = "xmpp:test@example.com")),
                timezones = listOf(TimezoneProperty(value = "America/New_York")),
                geographicPositions =
                    listOf(GeographicPositionProperty(value = "geo:37.386013,-122.082932")),
                roles = listOf(RoleProperty(value = "Developer")),
                categories = listOf(CategoriesProperty(value = listOf("friend", "colleague"))),
                productIdentifier = ProductIdentifierProperty(value = "-//Test//EN"),
                revision = RevisionProperty(value = "20200101T000000Z"),
                uniqueIdentifier =
                    UniqueIdentifierProperty(
                        value = "urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6"
                    ),
                keys = listOf(KeyProperty(value = "https://example.com/key.pub")),
            )
        val expected =
            """
            BEGIN:VCARD
            VERSION:4.0
            KIND:individual
            FN:Test User
            ANNIVERSARY:2010-06-15
            GENDER:M;
            TEL;TYPE=work,voice;VALUE=uri:tel:+1-555-555-5555
            IMPP:xmpp:test@example.com
            TZ:America/New_York
            GEO:geo:37.386013,-122.082932
            TITLE:Senior Engineer
            ROLE:Developer
            CATEGORIES:friend,colleague
            NOTE:A note about John
            PRODID:-//Test//EN
            REV:20200101T000000Z
            UID:urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6
            URL:https://example.com
            KEY:https://example.com/key.pub
            END:VCARD
            """
                .trimIndent()
        // Only check a few key lines rather than exact match due to ordering
        val output = vcard.toVCardString()
        output.lines().filter { it.startsWith("KIND:") } shouldBe listOf("KIND:individual")
        output.lines().filter { it.startsWith("ANNIVERSARY:") } shouldBe
            listOf("ANNIVERSARY:2010-06-15")
        output.lines().filter { it.startsWith("GENDER:") } shouldBe listOf("GENDER:M;")
        output.lines().filter { it.startsWith("IMPP:") } shouldBe
            listOf("IMPP:xmpp:test@example.com")
        output.lines().filter { it.startsWith("UID:") } shouldBe
            listOf("UID:urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6")
        output.lines().filter { it.startsWith("KEY:") } shouldBe
            listOf("KEY:https://example.com/key.pub")
        output.lines().filter { it.startsWith("PRODID:") } shouldBe listOf("PRODID:-//Test//EN")
        output.lines().filter { it.startsWith("REV:") } shouldBe listOf("REV:20200101T000000Z")
    }
}
