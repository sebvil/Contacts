package com.sebastianvm.contacts.vcard

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.CalscaleParameter
import com.sebastianvm.contacts.vcard.parameters.LanguageParameter
import com.sebastianvm.contacts.vcard.parameters.MediatypeParameter
import com.sebastianvm.contacts.vcard.parameters.PidParameter
import com.sebastianvm.contacts.vcard.parameters.PrefParameter
import com.sebastianvm.contacts.vcard.parameters.SortAsParameter
import com.sebastianvm.contacts.vcard.parameters.TypeParameter
import com.sebastianvm.contacts.vcard.parameters.Value
import com.sebastianvm.contacts.vcard.parameters.ValueParameter
import com.sebastianvm.contacts.vcard.properties.AddressProperty
import com.sebastianvm.contacts.vcard.properties.AnniversaryProperty
import com.sebastianvm.contacts.vcard.properties.BirthdayProperty
import com.sebastianvm.contacts.vcard.properties.CalendarAddressUriProperty
import com.sebastianvm.contacts.vcard.properties.CalendarUriProperty
import com.sebastianvm.contacts.vcard.properties.CategoriesProperty
import com.sebastianvm.contacts.vcard.properties.ClientPidMapProperty
import com.sebastianvm.contacts.vcard.properties.EmailProperty
import com.sebastianvm.contacts.vcard.properties.FreeBusyUrlProperty
import com.sebastianvm.contacts.vcard.properties.FormattedNameProperty
import com.sebastianvm.contacts.vcard.properties.GenderProperty
import com.sebastianvm.contacts.vcard.properties.GeographicPositionProperty
import com.sebastianvm.contacts.vcard.properties.InstantMessagingProperty
import com.sebastianvm.contacts.vcard.properties.KeyProperty
import com.sebastianvm.contacts.vcard.properties.KindProperty
import com.sebastianvm.contacts.vcard.properties.LanguageProperty
import com.sebastianvm.contacts.vcard.properties.LogoProperty
import com.sebastianvm.contacts.vcard.properties.MemberProperty
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
import com.sebastianvm.contacts.vcard.properties.TitleProperty
import com.sebastianvm.contacts.vcard.properties.TimezoneProperty
import com.sebastianvm.contacts.vcard.properties.UniqueIdentifierProperty
import com.sebastianvm.contacts.vcard.properties.UrlProperty
import com.sebastianvm.contacts.vcard.properties.XmlProperty
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

val PropertyEncodingTest by testSuite {

    // Existing properties

    test("FN with altId and pid") {
        FormattedNameProperty(value = "John", altId = AltIdParameter("1"), pid = PidParameter("1.1"))
            .toVCardString() shouldBe "FN;ALTID=1;PID=1.1:John"
    }

    test("N with sortAs") {
        NameProperty(
                value = listOf("Doe", "John", "", "", ""),
                sortAs = SortAsParameter(listOf("Doe", "John")),
            )
            .toVCardString() shouldBe """N;SORT-AS="Doe,John":Doe;John;;;"""
    }

    test("N escapes semicolons in components") {
        NameProperty(value = listOf("O;Brien", "John", "", "", "")).toVCardString() shouldBe
            "N:O\\;Brien;John;;;"
    }

    test("EMAIL without language") {
        EmailProperty(value = "john@example.com", type = TypeParameter(listOf("work")))
            .toVCardString() shouldBe "EMAIL;TYPE=work:john@example.com"
    }

    test("TEL with mediatype") {
        TelephoneProperty(
                value = "tel:+1-555",
                type = TypeParameter(listOf("voice")),
                valueParam = ValueParameter(Value.Uri),
                mediatype = MediatypeParameter("audio/basic"),
            )
            .toVCardString() shouldBe "TEL;TYPE=voice;VALUE=uri;MEDIATYPE=audio/basic:tel:+1-555"
    }

    test("ADR with all new params") {
        AddressProperty(
                value = listOf("", "", "123 Main", "City", "ST", "12345", "US"),
                type = TypeParameter(listOf("home")),
            )
            .toVCardString() shouldBe "ADR;TYPE=home:;;123 Main;City;ST;12345;US"
    }

    test("ORG with sortAs") {
        OrganizationProperty(value = listOf("Acme", "Engineering"), sortAs = SortAsParameter(listOf("Acme")))
            .toVCardString() shouldBe """ORG;SORT-AS="Acme":Acme;Engineering"""
    }

    test("BDAY without language with calscale") {
        BirthdayProperty(value = "1990-01-15", calscale = CalscaleParameter("gregorian"))
            .toVCardString() shouldBe "BDAY;CALSCALE=gregorian:1990-01-15"
    }

    test("PHOTO with mediatype") {
        PhotoProperty(
                value = "https://example.com/photo.jpg",
                mediatype = MediatypeParameter("image/jpeg"),
            )
            .toVCardString() shouldBe "PHOTO;MEDIATYPE=image/jpeg:https://example.com/photo.jpg"
    }

    test("URL with mediatype") {
        UrlProperty(value = "https://example.com", mediatype = MediatypeParameter("text/html"))
            .toVCardString() shouldBe "URL;MEDIATYPE=text/html:https://example.com"
    }

    test("NOTE with type") {
        NoteProperty(value = "A note", type = TypeParameter(listOf("work")))
            .toVCardString() shouldBe "NOTE;TYPE=work:A note"
    }


    // New properties

    test("ANNIVERSARY") {
        AnniversaryProperty(value = "2010-06-15").toVCardString() shouldBe "ANNIVERSARY:2010-06-15"
    }

    test("GENDER with sex and text") {
        GenderProperty(value = listOf("M", "male")).toVCardString() shouldBe "GENDER:M;male"
    }

    test("GENDER with sex only") {
        GenderProperty(value = listOf("F")).toVCardString() shouldBe "GENDER:F"
    }

    test("IMPP") {
        InstantMessagingProperty(value = "xmpp:test@example.com", type = TypeParameter(listOf("home")))
            .toVCardString() shouldBe "IMPP;TYPE=home:xmpp:test@example.com"
    }

    test("LANG") {
        LanguageProperty(value = "en", pref = PrefParameter(1)).toVCardString() shouldBe
            "LANG;PREF=1:en"
    }

    test("TZ property") {
        TimezoneProperty(value = "America/New_York").toVCardString() shouldBe "TZ:America/New_York"
    }

    test("GEO property") {
        GeographicPositionProperty(value = "geo:37.386013,-122.082932").toVCardString() shouldBe
            "GEO:geo:37.386013,-122.082932"
    }

    test("TITLE with all params") {
        TitleProperty(
                value = "Engineer",
                type = TypeParameter(listOf("work")),
                pref = PrefParameter(1),
                pid = PidParameter("1.1"),
                altId = AltIdParameter("1"),
                language = LanguageParameter("en"),
            )
            .toVCardString() shouldBe "TITLE;TYPE=work;PREF=1;LANGUAGE=en;ALTID=1;PID=1.1:Engineer"
    }

    test("ROLE with all params") {
        RoleProperty(
                value = "Developer",
                type = TypeParameter(listOf("work")),
                pref = PrefParameter(1),
                pid = PidParameter("1.1"),
                altId = AltIdParameter("1"),
                language = LanguageParameter("en"),
            )
            .toVCardString() shouldBe "ROLE;LANGUAGE=en;PID=1.1;PREF=1;TYPE=work;ALTID=1:Developer"
    }

    test("LOGO") {
        LogoProperty(
                value = "https://example.com/logo.png",
                mediatype = MediatypeParameter("image/png"),
            )
            .toVCardString() shouldBe "LOGO;MEDIATYPE=image/png:https://example.com/logo.png"
    }

    test("MEMBER") {
        MemberProperty(value = "urn:uuid:abc123").toVCardString() shouldBe "MEMBER:urn:uuid:abc123"
    }

    test("RELATED") {
        RelatedProperty(value = "urn:uuid:abc123", type = TypeParameter(listOf("spouse")))
            .toVCardString() shouldBe "RELATED;TYPE=spouse:urn:uuid:abc123"
    }

    test("CATEGORIES escaping") {
        CategoriesProperty(value = listOf("friend, work", "family;home")).toVCardString() shouldBe
            "CATEGORIES:friend\\, work,family\\;home"
    }

    test("CATEGORIES with all params") {
        CategoriesProperty(
                value = listOf("friend"),
                type = TypeParameter(listOf("work")),
                pref = PrefParameter(1),
                pid = PidParameter("1.1"),
                altId = AltIdParameter("1"),
            )
            .toVCardString() shouldBe "CATEGORIES;PID=1.1;PREF=1;TYPE=work;ALTID=1:friend"
    }

    test("NOTE escaping") {
        NoteProperty(value = "Line 1\nLine 2; and comma, and backslash\\")
            .toVCardString() shouldBe "NOTE:Line 1\\nLine 2\\; and comma\\, and backslash\\\\"
    }

    test("NOTE with all params") {
        NoteProperty(
                value = "A note",
                language = LanguageParameter("en"),
                type = TypeParameter(listOf("work")),
                pref = PrefParameter(1),
                pid = PidParameter("1.1"),
                altId = AltIdParameter("1"),
            )
            .toVCardString() shouldBe "NOTE;TYPE=work;PREF=1;LANGUAGE=en;ALTID=1;PID=1.1:A note"
    }

    test("PRODID") {
        ProductIdentifierProperty(value = "-//Test//EN").toVCardString() shouldBe "PRODID:-//Test//EN"
    }

    test("REV") {
        RevisionProperty(value = "20200101T000000Z").toVCardString() shouldBe "REV:20200101T000000Z"
    }

    test("SOUND") {
        SoundProperty(
                value = "https://example.com/name.ogg",
                mediatype = MediatypeParameter("audio/ogg"),
            )
            .toVCardString() shouldBe "SOUND;MEDIATYPE=audio/ogg:https://example.com/name.ogg"
    }

    test("UID") {
        UniqueIdentifierProperty(value = "urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6")
            .toVCardString() shouldBe "UID:urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6"
    }

    test("CLIENTPIDMAP") {
        ClientPidMapProperty(value = listOf("1", "urn:uuid:abc")).toVCardString() shouldBe
            "CLIENTPIDMAP:1;urn:uuid:abc"
    }

    test("KEY") {
        KeyProperty(value = "https://example.com/key.pub", valueParam = ValueParameter(Value.Uri))
            .toVCardString() shouldBe "KEY;VALUE=uri:https://example.com/key.pub"
    }

    test("FBURL") {
        FreeBusyUrlProperty(value = "https://example.com/fb", type = TypeParameter(listOf("home")))
            .toVCardString() shouldBe "FBURL;TYPE=home:https://example.com/fb"
    }

    test("CALADRURI") {
        CalendarAddressUriProperty(value = "mailto:cal@example.com").toVCardString() shouldBe
            "CALADRURI:mailto:cal@example.com"
    }

    test("CALURI") {
        CalendarUriProperty(value = "https://example.com/cal").toVCardString() shouldBe
            "CALURI:https://example.com/cal"
    }

    test("SOURCE") {
        SourceProperty(value = "ldap://ldap.example.com/o=Acme").toVCardString() shouldBe
            "SOURCE:ldap://ldap.example.com/o=Acme"
    }

    test("KIND") { KindProperty(value = "individual").toVCardString() shouldBe "KIND:individual" }

    test("XML") {
        XmlProperty(value = "<custom>data</custom>").toVCardString() shouldBe
            "XML:<custom>data</custom>"
    }

    test("NICKNAME basic") {
        NicknameProperty(value = listOf("Johnny")).toVCardString() shouldBe "NICKNAME:Johnny"
    }

    test("NICKNAME with all params") {
        NicknameProperty(
                value = listOf("Johnny", "John"),
                type = TypeParameter(listOf("work")),
                pref = PrefParameter(1),
                pid = PidParameter("1.1"),
                altId = AltIdParameter("1"),
                language = LanguageParameter("en"),
            )
            .toVCardString() shouldBe "NICKNAME;TYPE=work;PREF=1;LANGUAGE=en;ALTID=1;PID=1.1:Johnny,John"
    }
}
