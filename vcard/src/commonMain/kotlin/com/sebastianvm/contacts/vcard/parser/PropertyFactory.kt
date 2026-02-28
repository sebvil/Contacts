package com.sebastianvm.contacts.vcard.parser

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.CalscaleParameter
import com.sebastianvm.contacts.vcard.parameters.GeoParameter
import com.sebastianvm.contacts.vcard.parameters.LabelParameter
import com.sebastianvm.contacts.vcard.parameters.LanguageParameter
import com.sebastianvm.contacts.vcard.parameters.MediatypeParameter
import com.sebastianvm.contacts.vcard.parameters.PidParameter
import com.sebastianvm.contacts.vcard.parameters.PrefParameter
import com.sebastianvm.contacts.vcard.parameters.SortAsParameter
import com.sebastianvm.contacts.vcard.parameters.TypeParameter
import com.sebastianvm.contacts.vcard.parameters.TzParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter
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
import com.sebastianvm.contacts.vcard.properties.TimezoneProperty
import com.sebastianvm.contacts.vcard.properties.TitleProperty
import com.sebastianvm.contacts.vcard.properties.UniqueIdentifierProperty
import com.sebastianvm.contacts.vcard.properties.UrlProperty
import com.sebastianvm.contacts.vcard.properties.VCardProperty
import com.sebastianvm.contacts.vcard.properties.XmlProperty
import com.sebastianvm.contacts.vcard.splitListValue
import com.sebastianvm.contacts.vcard.splitStructuredValue
import com.sebastianvm.contacts.vcard.unescapeVCardText

@Suppress("TooManyFunctions")
object PropertyFactory {

    fun create(contentLine: ContentLine): VCardProperty<*>? {
        val params = ParameterFactory.createAll(contentLine.parameters)
        return when (contentLine.name) {
            "FN" -> createFormattedName(contentLine.value, params)
            "N" -> createName(contentLine.value, params)
            "EMAIL" -> createEmailAddress(contentLine.value, params)
            "TEL" -> createTelephoneNumber(contentLine.value, params)
            "ADR" -> createPhysicalAddress(contentLine.value, params)
            "ORG" -> createOrganization(contentLine.value, params)
            "TITLE" -> createTitle(contentLine.value, params)
            "NOTE" -> createNote(contentLine.value, params)
            "URL" -> createUrl(contentLine.value, params)
            "BDAY" -> createBirthday(contentLine.value, params)
            "NICKNAME" -> createNickname(contentLine.value, params)
            "PHOTO" -> createPhoto(contentLine.value, params)
            "ANNIVERSARY" -> createAnniversary(contentLine.value, params)
            "GENDER" -> createGender(contentLine.value, params)
            "IMPP" -> createInstantMessagingAddress(contentLine.value, params)
            "LANG" -> createSpokenLanguage(contentLine.value, params)
            "TZ" -> createTimezone(contentLine.value, params)
            "GEO" -> createGeographicPosition(contentLine.value, params)
            "ROLE" -> createRole(contentLine.value, params)
            "LOGO" -> createLogo(contentLine.value, params)
            "MEMBER" -> createMember(contentLine.value, params)
            "RELATED" -> createRelated(contentLine.value, params)
            "CATEGORIES" -> createCategories(contentLine.value, params)
            "PRODID" -> createProductIdentifier(contentLine.value, params)
            "REV" -> createRevision(contentLine.value, params)
            "SOUND" -> createSound(contentLine.value, params)
            "UID" -> createUniqueIdentifier(contentLine.value, params)
            "CLIENTPIDMAP" -> createClientPidMap(contentLine.value)
            "KEY" -> createKey(contentLine.value, params)
            "FBURL" -> createFreeBusyUrl(contentLine.value, params)
            "CALADRURI" -> createCalendarAddressUri(contentLine.value, params)
            "CALURI" -> createCalendarUri(contentLine.value, params)
            "SOURCE" -> createSource(contentLine.value, params)
            "KIND" -> createKind(contentLine.value, params)
            "XML" -> createXml(contentLine.value, params)
            else -> null
        }
    }

    private inline fun <reified T> List<VCardPropertyParameter<*>>.find(): T? =
        filterIsInstance<T>().firstOrNull()

    private fun createFormattedName(value: String, params: List<VCardPropertyParameter<*>>) =
        FormattedNameProperty(
            value = unescapeVCardText(value),
            valueParam = params.find<ValueParameter>(),
            type = params.find<TypeParameter>(),
            pref = params.find<PrefParameter>(),
            language = params.find<LanguageParameter>(),
            altId = params.find<AltIdParameter>(),
            pid = params.find<PidParameter>(),
        )

    private fun createName(value: String, params: List<VCardPropertyParameter<*>>) =
        NameProperty(
            value = splitStructuredValue(value),
            valueParam = params.find<ValueParameter>(),
            sortAs = params.find<SortAsParameter>(),
            language = params.find<LanguageParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createEmailAddress(value: String, params: List<VCardPropertyParameter<*>>) =
        EmailProperty(
            value = unescapeVCardText(value),
            valueParam = params.find<ValueParameter>(),
            type = params.find<TypeParameter>(),
            pref = params.find<PrefParameter>(),
            altId = params.find<AltIdParameter>(),
            pid = params.find<PidParameter>(),
        )

    private fun createTelephoneNumber(value: String, params: List<VCardPropertyParameter<*>>) =
        TelephoneProperty(
            value = unescapeVCardText(value),
            type = params.find<TypeParameter>(),
            valueParam = params.find<ValueParameter>(),
            pref = params.find<PrefParameter>(),
            mediatype = params.find<MediatypeParameter>(),
            altId = params.find<AltIdParameter>(),
            pid = params.find<PidParameter>(),
        )

    private fun createPhysicalAddress(value: String, params: List<VCardPropertyParameter<*>>) =
        AddressProperty(
            value = splitStructuredValue(value),
            valueParam = params.find<ValueParameter>(),
            type = params.find<TypeParameter>(),
            pref = params.find<PrefParameter>(),
            language = params.find<LanguageParameter>(),
            label = params.find<LabelParameter>(),
            geo = params.find<GeoParameter>(),
            tz = params.find<TzParameter>(),
            altId = params.find<AltIdParameter>(),
            pid = params.find<PidParameter>(),
        )

    private fun createOrganization(value: String, params: List<VCardPropertyParameter<*>>) =
        OrganizationProperty(
            value = splitStructuredValue(value),
            valueParam = params.find<ValueParameter>(),
            sortAs = params.find<SortAsParameter>(),
            type = params.find<TypeParameter>(),
            pref = params.find<PrefParameter>(),
            language = params.find<LanguageParameter>(),
            altId = params.find<AltIdParameter>(),
            pid = params.find<PidParameter>(),
        )

    private fun createTitle(value: String, params: List<VCardPropertyParameter<*>>) =
        TitleProperty(
            value = unescapeVCardText(value),
            valueParam = params.find<ValueParameter>(),
            type = params.find<TypeParameter>(),
            pref = params.find<PrefParameter>(),
            language = params.find<LanguageParameter>(),
            altId = params.find<AltIdParameter>(),
            pid = params.find<PidParameter>(),
        )

    private fun createNote(value: String, params: List<VCardPropertyParameter<*>>) =
        NoteProperty(
            value = unescapeVCardText(value),
            valueParam = params.find<ValueParameter>(),
            type = params.find<TypeParameter>(),
            pref = params.find<PrefParameter>(),
            language = params.find<LanguageParameter>(),
            altId = params.find<AltIdParameter>(),
            pid = params.find<PidParameter>(),
        )

    private fun createUrl(value: String, params: List<VCardPropertyParameter<*>>) =
        UrlProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            type = params.find<TypeParameter>(),
            pref = params.find<PrefParameter>(),
            mediatype = params.find<MediatypeParameter>(),
            altId = params.find<AltIdParameter>(),
            pid = params.find<PidParameter>(),
        )

    private fun createBirthday(value: String, params: List<VCardPropertyParameter<*>>) =
        BirthdayProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            altId = params.find<AltIdParameter>(),
            calscale = params.find<CalscaleParameter>(),
        )

    private fun createNickname(value: String, params: List<VCardPropertyParameter<*>>) =
        NicknameProperty(
            value = splitListValue(value),
            valueParam = params.find<ValueParameter>(),
            type = params.find<TypeParameter>(),
            pref = params.find<PrefParameter>(),
            language = params.find<LanguageParameter>(),
            altId = params.find<AltIdParameter>(),
            pid = params.find<PidParameter>(),
        )

    private fun createPhoto(value: String, params: List<VCardPropertyParameter<*>>) =
        PhotoProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            type = params.find<TypeParameter>(),
            pref = params.find<PrefParameter>(),
            mediatype = params.find<MediatypeParameter>(),
            altId = params.find<AltIdParameter>(),
            pid = params.find<PidParameter>(),
        )

    private fun createAnniversary(value: String, params: List<VCardPropertyParameter<*>>) =
        AnniversaryProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            altId = params.find<AltIdParameter>(),
            calscale = params.find<CalscaleParameter>(),
        )

    private fun createGender(value: String, params: List<VCardPropertyParameter<*>>) =
        GenderProperty(
            value = splitStructuredValue(value),
            valueParam = params.find<ValueParameter>(),
        )

    private fun createInstantMessagingAddress(
        value: String,
        params: List<VCardPropertyParameter<*>>,
    ) =
        InstantMessagingProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            pid = params.find<PidParameter>(),
            pref = params.find<PrefParameter>(),
            type = params.find<TypeParameter>(),
            mediatype = params.find<MediatypeParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createSpokenLanguage(value: String, params: List<VCardPropertyParameter<*>>) =
        LanguageProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            pid = params.find<PidParameter>(),
            pref = params.find<PrefParameter>(),
            type = params.find<TypeParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createTimezone(value: String, params: List<VCardPropertyParameter<*>>) =
        TimezoneProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            pid = params.find<PidParameter>(),
            pref = params.find<PrefParameter>(),
            type = params.find<TypeParameter>(),
            mediatype = params.find<MediatypeParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createGeographicPosition(value: String, params: List<VCardPropertyParameter<*>>) =
        GeographicPositionProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            pid = params.find<PidParameter>(),
            pref = params.find<PrefParameter>(),
            type = params.find<TypeParameter>(),
            mediatype = params.find<MediatypeParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createRole(value: String, params: List<VCardPropertyParameter<*>>) =
        RoleProperty(
            value = unescapeVCardText(value),
            valueParam = params.find<ValueParameter>(),
            language = params.find<LanguageParameter>(),
            pid = params.find<PidParameter>(),
            pref = params.find<PrefParameter>(),
            type = params.find<TypeParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createLogo(value: String, params: List<VCardPropertyParameter<*>>) =
        LogoProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            language = params.find<LanguageParameter>(),
            pid = params.find<PidParameter>(),
            pref = params.find<PrefParameter>(),
            type = params.find<TypeParameter>(),
            mediatype = params.find<MediatypeParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createMember(value: String, params: List<VCardPropertyParameter<*>>) =
        MemberProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            pid = params.find<PidParameter>(),
            pref = params.find<PrefParameter>(),
            mediatype = params.find<MediatypeParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createRelated(value: String, params: List<VCardPropertyParameter<*>>) =
        RelatedProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            mediatype = params.find<MediatypeParameter>(),
            pid = params.find<PidParameter>(),
            pref = params.find<PrefParameter>(),
            type = params.find<TypeParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createCategories(value: String, params: List<VCardPropertyParameter<*>>) =
        CategoriesProperty(
            value = splitListValue(value),
            valueParam = params.find<ValueParameter>(),
            pid = params.find<PidParameter>(),
            pref = params.find<PrefParameter>(),
            type = params.find<TypeParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createProductIdentifier(value: String, params: List<VCardPropertyParameter<*>>) =
        ProductIdentifierProperty(value = value, valueParam = params.find<ValueParameter>())

    private fun createRevision(value: String, params: List<VCardPropertyParameter<*>>) =
        RevisionProperty(value = value, valueParam = params.find<ValueParameter>())

    private fun createSound(value: String, params: List<VCardPropertyParameter<*>>) =
        SoundProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            language = params.find<LanguageParameter>(),
            pid = params.find<PidParameter>(),
            pref = params.find<PrefParameter>(),
            type = params.find<TypeParameter>(),
            mediatype = params.find<MediatypeParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createUniqueIdentifier(value: String, params: List<VCardPropertyParameter<*>>) =
        UniqueIdentifierProperty(value = value, valueParam = params.find<ValueParameter>())

    private fun createClientPidMap(value: String) =
        ClientPidMapProperty(value = splitStructuredValue(value))

    private fun createKey(value: String, params: List<VCardPropertyParameter<*>>) =
        KeyProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            pid = params.find<PidParameter>(),
            pref = params.find<PrefParameter>(),
            type = params.find<TypeParameter>(),
            mediatype = params.find<MediatypeParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createFreeBusyUrl(value: String, params: List<VCardPropertyParameter<*>>) =
        FreeBusyUrlProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            mediatype = params.find<MediatypeParameter>(),
            pid = params.find<PidParameter>(),
            pref = params.find<PrefParameter>(),
            type = params.find<TypeParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createCalendarAddressUri(value: String, params: List<VCardPropertyParameter<*>>) =
        CalendarAddressUriProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            pid = params.find<PidParameter>(),
            pref = params.find<PrefParameter>(),
            type = params.find<TypeParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createCalendarUri(value: String, params: List<VCardPropertyParameter<*>>) =
        CalendarUriProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            mediatype = params.find<MediatypeParameter>(),
            pid = params.find<PidParameter>(),
            pref = params.find<PrefParameter>(),
            type = params.find<TypeParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createSource(value: String, params: List<VCardPropertyParameter<*>>) =
        SourceProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            pid = params.find<PidParameter>(),
            pref = params.find<PrefParameter>(),
            mediatype = params.find<MediatypeParameter>(),
            altId = params.find<AltIdParameter>(),
        )

    private fun createKind(value: String, params: List<VCardPropertyParameter<*>>) =
        KindProperty(value = value, valueParam = params.find<ValueParameter>())

    private fun createXml(value: String, params: List<VCardPropertyParameter<*>>) =
        XmlProperty(
            value = value,
            valueParam = params.find<ValueParameter>(),
            altId = params.find<AltIdParameter>(),
        )
}
