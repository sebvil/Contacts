package com.sebastianvm.contacts.vcard

import com.sebastianvm.contacts.vcard.parameters.LanguageParameter
import com.sebastianvm.contacts.vcard.parameters.PrefParameter
import com.sebastianvm.contacts.vcard.parameters.TypeParameter
import com.sebastianvm.contacts.vcard.parameters.Value
import com.sebastianvm.contacts.vcard.parameters.ValueParameter
import com.sebastianvm.contacts.vcard.parser.VCardParseResult
import com.sebastianvm.contacts.vcard.parser.parse
import com.sebastianvm.contacts.vcard.properties.AddressProperty
import com.sebastianvm.contacts.vcard.properties.AnniversaryProperty
import com.sebastianvm.contacts.vcard.properties.BirthdayProperty
import com.sebastianvm.contacts.vcard.properties.CalendarAddressUriProperty
import com.sebastianvm.contacts.vcard.properties.CalendarUriProperty
import com.sebastianvm.contacts.vcard.properties.CategoriesProperty
import com.sebastianvm.contacts.vcard.properties.ClientPidMapProperty
import com.sebastianvm.contacts.vcard.properties.EmailProperty
import com.sebastianvm.contacts.vcard.properties.FormattedNameProperty
import com.sebastianvm.contacts.vcard.properties.FreeBusyUrlProperty
import com.sebastianvm.contacts.vcard.properties.GenderProperty
import com.sebastianvm.contacts.vcard.properties.GeographicPositionProperty
import com.sebastianvm.contacts.vcard.properties.InstantMessagingProperty
import com.sebastianvm.contacts.vcard.properties.KeyProperty
import com.sebastianvm.contacts.vcard.properties.KindProperty
import com.sebastianvm.contacts.vcard.properties.LanguageProperty
import com.sebastianvm.contacts.vcard.properties.NameProperty
import com.sebastianvm.contacts.vcard.properties.NicknameProperty
import com.sebastianvm.contacts.vcard.properties.NoteProperty
import com.sebastianvm.contacts.vcard.properties.OrganizationProperty
import com.sebastianvm.contacts.vcard.properties.PhotoProperty
import com.sebastianvm.contacts.vcard.properties.ProductIdentifierProperty
import com.sebastianvm.contacts.vcard.properties.RelatedProperty
import com.sebastianvm.contacts.vcard.properties.RevisionProperty
import com.sebastianvm.contacts.vcard.properties.RoleProperty
import com.sebastianvm.contacts.vcard.properties.SoundProperty
import com.sebastianvm.contacts.vcard.properties.SourceProperty
import com.sebastianvm.contacts.vcard.properties.TelephoneProperty
import com.sebastianvm.contacts.vcard.properties.TimezoneProperty
import com.sebastianvm.contacts.vcard.properties.TitleProperty
import com.sebastianvm.contacts.vcard.properties.UniqueIdentifierProperty
import com.sebastianvm.contacts.vcard.properties.UrlProperty
import com.sebastianvm.contacts.vcard.properties.XmlProperty
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

val VCardRoundtripTest by testSuite {
    test("roundtrips minimal v4 vcard") {
        val original = V4VCard(formattedName = FormattedNameProperty(value = "John Doe"))
        val encoded = original.toVCardString()
        val result = VCard.parse(encoded)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        val parsed = result.vcards[0] as V4VCard
        parsed.formattedName shouldBe original.formattedName
    }

    test("roundtrips v4 vcard with language") {
        val original =
            V4VCard(
                formattedName =
                    FormattedNameProperty(value = "John Doe", language = LanguageParameter("en"))
            )
        val encoded = original.toVCardString()
        val result = VCard.parse(encoded)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        val parsed = result.vcards[0] as V4VCard
        parsed.formattedName shouldBe original.formattedName
    }

    test("roundtrips v4 vcard with original 12 properties") {
        val original =
            V4VCard(
                formattedName = FormattedNameProperty(value = "John Doe"),
                name = NameProperty(value = listOf("Doe", "John", "Philip", "Mr.", "Jr.")),
                emailAddresses =
                    listOf(
                        EmailProperty(
                            value = "john@work.com",
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

        val encoded = original.toVCardString()
        val result = VCard.parse(encoded)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        val parsed = result.vcards[0] as V4VCard

        parsed.formattedName shouldBe original.formattedName
        parsed.name shouldBe original.name
        parsed.emailAddresses shouldBe original.emailAddresses
        parsed.telephoneNumbers shouldBe original.telephoneNumbers
        parsed.physicalAddresses shouldBe original.physicalAddresses
        parsed.organization shouldBe original.organization
        parsed.title shouldBe original.title
        parsed.note shouldBe original.note
        parsed.urls shouldBe original.urls
        parsed.birthday shouldBe original.birthday
        parsed.nicknames shouldBe original.nicknames
        parsed.photo shouldBe original.photo
    }

    test("roundtrips v4 vcard with all new properties") {
        val original =
            V4VCard(
                formattedName = FormattedNameProperty(value = "Test User"),
                kind = KindProperty(value = "individual"),
                anniversary = AnniversaryProperty(value = "2010-06-15"),
                gender = GenderProperty(value = listOf("M", "male")),
                instantMessagingAddresses =
                    listOf(InstantMessagingProperty(value = "xmpp:test@example.com")),
                spokenLanguages = listOf(LanguageProperty(value = "en", pref = PrefParameter(1))),
                timezones = listOf(TimezoneProperty(value = "America/New_York")),
                geographicPositions =
                    listOf(GeographicPositionProperty(value = "geo:37.386013,-122.082932")),
                roles = listOf(RoleProperty(value = "Developer")),
                categories = listOf(CategoriesProperty(value = listOf("friend", "colleague"))),
                productIdentifier = ProductIdentifierProperty(value = "-//Test//EN"),
                revision = RevisionProperty(value = "20200101T000000Z"),
                uniqueIdentifier = UniqueIdentifierProperty(value = "urn:uuid:f81d4fae"),
                sounds = listOf(SoundProperty(value = "https://example.com/name.ogg")),
                clientPidMaps = listOf(ClientPidMapProperty(value = listOf("1", "urn:uuid:abc"))),
                keys = listOf(KeyProperty(value = "https://example.com/key.pub")),
                freeBusyUrls = listOf(FreeBusyUrlProperty(value = "https://example.com/fb")),
                calendarAddressUris =
                    listOf(CalendarAddressUriProperty(value = "mailto:cal@example.com")),
                calendarUris = listOf(CalendarUriProperty(value = "https://example.com/cal")),
                sources = listOf(SourceProperty(value = "ldap://ldap.example.com/o=Acme")),
                xmls = listOf(XmlProperty(value = "<custom>data</custom>")),
                relatedPeople =
                    listOf(
                        RelatedProperty(
                            value = "urn:uuid:abc",
                            type = TypeParameter(listOf("spouse")),
                        )
                    ),
                members = listOf(),
                logos = listOf(),
            )

        val encoded = original.toVCardString()
        val result = VCard.parse(encoded)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        val parsed = result.vcards[0] as V4VCard

        parsed.kind shouldBe original.kind
        parsed.anniversary shouldBe original.anniversary
        parsed.gender shouldBe original.gender
        parsed.instantMessagingAddresses shouldBe original.instantMessagingAddresses
        parsed.spokenLanguages shouldBe original.spokenLanguages
        parsed.timezones shouldBe original.timezones
        parsed.geographicPositions shouldBe original.geographicPositions
        parsed.roles shouldBe original.roles
        parsed.categories shouldBe original.categories
        parsed.productIdentifier shouldBe original.productIdentifier
        parsed.revision shouldBe original.revision
        parsed.uniqueIdentifier shouldBe original.uniqueIdentifier
        parsed.sounds shouldBe original.sounds
        parsed.clientPidMaps shouldBe original.clientPidMaps
        parsed.keys shouldBe original.keys
        parsed.freeBusyUrls shouldBe original.freeBusyUrls
        parsed.calendarAddressUris shouldBe original.calendarAddressUris
        parsed.calendarUris shouldBe original.calendarUris
        parsed.sources shouldBe original.sources
        parsed.xmls shouldBe original.xmls
        parsed.relatedPeople shouldBe original.relatedPeople
    }

    test("roundtrips structured property with special characters") {
        val original =
            V4VCard(
                formattedName = FormattedNameProperty(value = "Test"),
                name = NameProperty(value = listOf("O'Brien", "John", "", "", "")),
            )
        val encoded = original.toVCardString()
        val result = VCard.parse(encoded)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        val parsed = result.vcards[0] as V4VCard
        parsed.name shouldBe original.name
    }

    test("roundtrips v3 vcard with shared properties") {
        val original =
            V3VCard(
                formattedName = FormattedNameProperty(value = "Jane Smith"),
                emailAddresses = listOf(EmailProperty(value = "jane@example.com")),
                timezones = listOf(TimezoneProperty(value = "Europe/London")),
                roles = listOf(RoleProperty(value = "Manager")),
                uniqueIdentifier = UniqueIdentifierProperty(value = "urn:uuid:12345"),
                keys = listOf(KeyProperty(value = "https://example.com/key")),
            )
        val encoded = original.toVCardString()
        val result = VCard.parse(encoded)
        result.shouldBeInstanceOf<VCardParseResult.Success>()
        val parsed = result.vcards[0] as V3VCard
        parsed.formattedName shouldBe original.formattedName
        parsed.emailAddresses shouldBe original.emailAddresses
        parsed.timezones shouldBe original.timezones
        parsed.roles shouldBe original.roles
        parsed.uniqueIdentifier shouldBe original.uniqueIdentifier
        parsed.keys shouldBe original.keys
    }
}
