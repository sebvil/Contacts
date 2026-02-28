package com.sebastianvm.contacts.vcard.parser

import com.sebastianvm.contacts.vcard.V2VCard
import com.sebastianvm.contacts.vcard.V3VCard
import com.sebastianvm.contacts.vcard.V4VCard
import com.sebastianvm.contacts.vcard.VCard
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
import com.sebastianvm.contacts.vcard.properties.XmlProperty

sealed class VCardParseResult {
    data class Success(val vcards: List<VCard>) : VCardParseResult()

    data class Failure(val message: String) : VCardParseResult()
}

object VCardParser {

    fun parse(input: String): VCardParseResult {
        val lines = LineUnfolder.unfold(input)
        val vcards = mutableListOf<VCard>()
        var i = 0

        while (i < lines.size) {
            val line = lines[i]
            if (line.equals("BEGIN:VCARD", ignoreCase = true)) {
                i++
                val result = parseVCard(lines, i)
                if (result != null) {
                    vcards.add(result.first)
                    i = result.second
                } else {
                    return VCardParseResult.Failure("Failed to parse vCard starting at line $i")
                }
            } else {
                i++
            }
        }

        if (vcards.isEmpty()) {
            return VCardParseResult.Failure("No vCard found in input")
        }

        return VCardParseResult.Success(vcards)
    }

    private fun parseVCard(lines: List<String>, startIndex: Int): Pair<VCard, Int>? {
        var version: String? = null
        var formattedName: FormattedNameProperty? = null
        var name: NameProperty? = null
        val emailAddresses = mutableListOf<EmailProperty>()
        val telephoneNumbers = mutableListOf<TelephoneProperty>()
        val physicalAddresses = mutableListOf<AddressProperty>()
        var organization: OrganizationProperty? = null
        var title: TitleProperty? = null
        var note: NoteProperty? = null
        val urls = mutableListOf<UrlProperty>()
        var birthday: BirthdayProperty? = null
        val nicknames = mutableListOf<NicknameProperty>()
        var photo: PhotoProperty? = null
        // V4-only
        var anniversary: AnniversaryProperty? = null
        var gender: GenderProperty? = null
        var productIdentifier: ProductIdentifierProperty? = null
        var revision: RevisionProperty? = null
        var uniqueIdentifier: UniqueIdentifierProperty? = null
        var kind: KindProperty? = null
        val instantMessagingAddresses = mutableListOf<InstantMessagingProperty>()
        val spokenLanguages = mutableListOf<LanguageProperty>()
        val timezones = mutableListOf<TimezoneProperty>()
        val geographicPositions = mutableListOf<GeographicPositionProperty>()
        val roles = mutableListOf<RoleProperty>()
        val logos = mutableListOf<LogoProperty>()
        val members = mutableListOf<MemberProperty>()
        val relatedPeople = mutableListOf<RelatedProperty>()
        val categories = mutableListOf<CategoriesProperty>()
        val sounds = mutableListOf<SoundProperty>()
        val clientPidMaps = mutableListOf<ClientPidMapProperty>()
        val keys = mutableListOf<KeyProperty>()
        val freeBusyUrls = mutableListOf<FreeBusyUrlProperty>()
        val calendarAddressUris = mutableListOf<CalendarAddressUriProperty>()
        val calendarUris = mutableListOf<CalendarUriProperty>()
        val sources = mutableListOf<SourceProperty>()
        val xmls = mutableListOf<XmlProperty>()

        var i = startIndex
        while (i < lines.size) {
            val line = lines[i]
            if (line.equals("END:VCARD", ignoreCase = true)) {
                i++
                break
            }

            val contentLine = ContentLineParser.parse(line)

            when (contentLine.name) {
                "VERSION" -> version = contentLine.value
                else -> {
                    val property = PropertyFactory.create(contentLine)
                    when (property) {
                        is FormattedNameProperty -> formattedName = property
                        is NameProperty -> name = property
                        is EmailProperty -> emailAddresses.add(property)
                        is TelephoneProperty -> telephoneNumbers.add(property)
                        is AddressProperty -> physicalAddresses.add(property)
                        is OrganizationProperty -> organization = property
                        is TitleProperty -> title = property
                        is NoteProperty -> note = property
                        is UrlProperty -> urls.add(property)
                        is BirthdayProperty -> birthday = property
                        is NicknameProperty -> nicknames.add(property)
                        is PhotoProperty -> photo = property
                        is AnniversaryProperty -> anniversary = property
                        is GenderProperty -> gender = property
                        is InstantMessagingProperty -> instantMessagingAddresses.add(property)
                        is LanguageProperty -> spokenLanguages.add(property)
                        is TimezoneProperty -> timezones.add(property)
                        is GeographicPositionProperty -> geographicPositions.add(property)
                        is RoleProperty -> roles.add(property)
                        is LogoProperty -> logos.add(property)
                        is MemberProperty -> members.add(property)
                        is RelatedProperty -> relatedPeople.add(property)
                        is CategoriesProperty -> categories.add(property)
                        is ProductIdentifierProperty -> productIdentifier = property
                        is RevisionProperty -> revision = property
                        is SoundProperty -> sounds.add(property)
                        is UniqueIdentifierProperty -> uniqueIdentifier = property
                        is ClientPidMapProperty -> clientPidMaps.add(property)
                        is KeyProperty -> keys.add(property)
                        is FreeBusyUrlProperty -> freeBusyUrls.add(property)
                        is CalendarAddressUriProperty -> calendarAddressUris.add(property)
                        is CalendarUriProperty -> calendarUris.add(property)
                        is SourceProperty -> sources.add(property)
                        is KindProperty -> kind = property
                        is XmlProperty -> xmls.add(property)
                        null -> {}
                    }
                }
            }
            i++
        }

        val vcard =
            when (version) {
                "4.0" -> {
                    formattedName ?: return null
                    V4VCard(
                        formattedName = formattedName,
                        name = name,
                        emailAddresses = emailAddresses,
                        telephoneNumbers = telephoneNumbers,
                        physicalAddresses = physicalAddresses,
                        organization = organization,
                        title = title,
                        note = note,
                        urls = urls,
                        birthday = birthday,
                        nicknames = nicknames,
                        photo = photo,
                        anniversary = anniversary,
                        gender = gender,
                        productIdentifier = productIdentifier,
                        revision = revision,
                        uniqueIdentifier = uniqueIdentifier,
                        kind = kind,
                        instantMessagingAddresses = instantMessagingAddresses,
                        spokenLanguages = spokenLanguages,
                        timezones = timezones,
                        geographicPositions = geographicPositions,
                        roles = roles,
                        logos = logos,
                        members = members,
                        relatedPeople = relatedPeople,
                        categories = categories,
                        sounds = sounds,
                        clientPidMaps = clientPidMaps,
                        keys = keys,
                        freeBusyUrls = freeBusyUrls,
                        calendarAddressUris = calendarAddressUris,
                        calendarUris = calendarUris,
                        sources = sources,
                        xmls = xmls,
                    )
                }
                "3.0" -> {
                    formattedName ?: return null
                    V3VCard(
                        formattedName = formattedName,
                        name = name,
                        emailAddresses = emailAddresses,
                        telephoneNumbers = telephoneNumbers,
                        physicalAddresses = physicalAddresses,
                        organization = organization,
                        title = title,
                        note = note,
                        urls = urls,
                        birthday = birthday,
                        nicknames = nicknames,
                        photo = photo,
                        timezones = timezones,
                        geographicPositions = geographicPositions,
                        roles = roles,
                        logos = logos,
                        categories = categories,
                        productIdentifier = productIdentifier,
                        revision = revision,
                        sounds = sounds,
                        uniqueIdentifier = uniqueIdentifier,
                        keys = keys,
                    )
                }
                "2.1" -> {
                    V2VCard(
                        formattedName = formattedName,
                        name = name,
                        emailAddresses = emailAddresses,
                        telephoneNumbers = telephoneNumbers,
                        physicalAddresses = physicalAddresses,
                        organization = organization,
                        title = title,
                        note = note,
                        urls = urls,
                        birthday = birthday,
                        nicknames = nicknames,
                        photo = photo,
                        timezones = timezones,
                        geographicPositions = geographicPositions,
                        roles = roles,
                        logos = logos,
                        revision = revision,
                        sounds = sounds,
                        uniqueIdentifier = uniqueIdentifier,
                        keys = keys,
                    )
                }
                else -> return null
            }

        return vcard to i
    }
}

fun VCard.Companion.parse(input: String): VCardParseResult = VCardParser.parse(input)
