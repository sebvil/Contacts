package com.sebastianvm.contacts.vcard

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
import com.sebastianvm.contacts.vcard.properties.VCardProperty
import com.sebastianvm.contacts.vcard.properties.XmlProperty

sealed class VCard : VCardComponent {
    abstract val version: String
    abstract val properties: List<VCardProperty<*>>

    override fun toVCardString(): String = buildString {
        appendLine("BEGIN:VCARD")
        appendLine("VERSION:$version")
        for (property in properties) {
            appendLine(property.toVCardString())
        }
        append("END:VCARD")
    }

    companion object
}

/**
 * Represents a vCard 4.0 object as defined in RFC 6350.
 */
data class V4VCard(
    /** The formatted name string associated with the vCard object (RFC 6350 6.2.1) */
    val formattedName: FormattedNameProperty,
    /** The components of the name of the object (RFC 6350 6.2.2) */
    val name: NameProperty? = null,
    /** The electronic mail addresses for communication with the object (RFC 6350 6.4.2) */
    val emailAddresses: List<EmailProperty> = emptyList(),
    /** The telephone numbers for communication with the object (RFC 6350 6.4.1) */
    val telephoneNumbers: List<TelephoneProperty> = emptyList(),
    /** The delivery addresses for the object (RFC 6350 6.3.1) */
    val physicalAddresses: List<AddressProperty> = emptyList(),
    /** The name and units of the organization associated with the object (RFC 6350 6.6.4) */
    val organization: OrganizationProperty? = null,
    /** The organizational title or position of the object (RFC 6350 6.6.1) */
    val title: TitleProperty? = null,
    /** Supplemental information or a comment that is associated with the vCard (RFC 6350 6.7.2) */
    val note: NoteProperty? = null,
    /** URLs that may be used to obtain real-time information about the object (RFC 6350 6.7.8) */
    val urls: List<UrlProperty> = emptyList(),
    /** The date of birth of the individual associated with the vCard (RFC 6350 6.2.5) */
    val birthday: BirthdayProperty? = null,
    /** The text-based nicknames of the object (RFC 6350 6.2.3) */
    val nicknames: List<NicknameProperty> = emptyList(),
    /** An image or photograph of the object (RFC 6350 6.2.4) */
    val photo: PhotoProperty? = null,
    /** The date of marriage, or equivalent, of the object (RFC 6350 6.2.6) */
    val anniversary: AnniversaryProperty? = null,
    /** The sex and gender identity of the object (RFC 6350 6.2.7) */
    val gender: GenderProperty? = null,
    /** The identifier for the product that created the vCard (RFC 6350 6.7.3) */
    val productIdentifier: ProductIdentifierProperty? = null,
    /** The revision date and time when the vCard was last updated (RFC 6350 6.7.4) */
    val revision: RevisionProperty? = null,
    /** A value that represents a globally unique identifier corresponding to the entity (RFC 6350 6.7.6) */
    val uniqueIdentifier: UniqueIdentifierProperty? = null,
    /** The type of entity that the vCard represents (RFC 6350 6.1.4) */
    val kind: KindProperty? = null,
    /** Instant Messaging and Presence Protocol addresses (RFC 6350 6.4.3) */
    val instantMessagingAddresses: List<InstantMessagingProperty> = emptyList(),
    /** The languages that may be used for contacting the entity (RFC 6350 6.4.4) */
    val spokenLanguages: List<LanguageProperty> = emptyList(),
    /** The time zones of the object (RFC 6350 6.5.1) */
    val timezones: List<TimezoneProperty> = emptyList(),
    /** The geographic positions of the object (RFC 6350 6.5.2) */
    val geographicPositions: List<GeographicPositionProperty> = emptyList(),
    /** The roles, occupations, or business categories of the object (RFC 6350 6.6.2) */
    val roles: List<RoleProperty> = emptyList(),
    /** The logos of the organization associated with the object (RFC 6350 6.6.3) */
    val logos: List<LogoProperty> = emptyList(),
    /** The members of the group represented by the vCard (RFC 6350 6.6.5) */
    val members: List<MemberProperty> = emptyList(),
    /** Other entities that the object is associated with (RFC 6350 6.6.6) */
    val relatedPeople: List<RelatedProperty> = emptyList(),
    /** The application categories that the object belongs to (RFC 6350 6.7.1) */
    val categories: List<CategoriesProperty> = emptyList(),
    /** Digital sound content that annotates some aspect of the object (RFC 6350 6.7.5) */
    val sounds: List<SoundProperty> = emptyList(),
    /** Mapping between a PID and its URI (RFC 6350 6.7.7) */
    val clientPidMaps: List<ClientPidMapProperty> = emptyList(),
    /** The public keys or certificates associated with the object (RFC 6350 6.8.1) */
    val keys: List<KeyProperty> = emptyList(),
    /** URLs for the entity's free/busy time (RFC 6350 6.9.1) */
    val freeBusyUrls: List<FreeBusyUrlProperty> = emptyList(),
    /** The URIs for the entity's calendar (RFC 6350 6.9.2) */
    val calendarAddressUris: List<CalendarAddressUriProperty> = emptyList(),
    /** The URIs for the entity's calendar (RFC 6350 6.9.3) */
    val calendarUris: List<CalendarUriProperty> = emptyList(),
    /** URIs that may be used to obtain the vCard (RFC 6350 6.1.3) */
    val sources: List<SourceProperty> = emptyList(),
    /** Any XML data that is associated with the vCard (RFC 6350 6.10.1) */
    val xmls: List<XmlProperty> = emptyList(),
) : VCard() {
    override val version: String = "4.0"
    override val properties: List<VCardProperty<*>> = buildList {
        // General
        kind?.let { add(it) }
        addAll(sources)
        addAll(xmls)
        // Identification
        add(formattedName)
        name?.let { add(it) }
        addAll(nicknames)
        photo?.let { add(it) }
        birthday?.let { add(it) }
        anniversary?.let { add(it) }
        gender?.let { add(it) }
        // Delivery Addressing
        addAll(physicalAddresses)
        // Communications
        addAll(telephoneNumbers)
        addAll(emailAddresses)
        addAll(instantMessagingAddresses)
        addAll(spokenLanguages)
        // Geographical
        addAll(timezones)
        addAll(geographicPositions)
        // Organizational
        title?.let { add(it) }
        addAll(roles)
        addAll(logos)
        organization?.let { add(it) }
        addAll(members)
        addAll(relatedPeople)
        // Explanatory
        addAll(categories)
        note?.let { add(it) }
        productIdentifier?.let { add(it) }
        revision?.let { add(it) }
        addAll(sounds)
        uniqueIdentifier?.let { add(it) }
        addAll(clientPidMaps)
        addAll(urls)
        // Security
        addAll(keys)
        // Calendar
        addAll(freeBusyUrls)
        addAll(calendarAddressUris)
        addAll(calendarUris)
    }
}

/**
 * Represents a vCard 3.0 object as defined in RFC 2426.
 */
data class V3VCard(
    /** The formatted name string associated with the vCard object (RFC 2426 3.1.1) */
    val formattedName: FormattedNameProperty,
    /** The components of the name of the object (RFC 2426 3.1.2) */
    val name: NameProperty? = null,
    /** The electronic mail addresses for communication with the object (RFC 2426 3.3.2) */
    val emailAddresses: List<EmailProperty> = emptyList(),
    /** The telephone numbers for communication with the object (RFC 2426 3.3.1) */
    val telephoneNumbers: List<TelephoneProperty> = emptyList(),
    /** The delivery addresses for the object (RFC 2426 3.2.1) */
    val physicalAddresses: List<AddressProperty> = emptyList(),
    /** The name and units of the organization associated with the object (RFC 2426 3.5.5) */
    val organization: OrganizationProperty? = null,
    /** The organizational title or position of the object (RFC 2426 3.5.1) */
    val title: TitleProperty? = null,
    /** Supplemental information or a comment that is associated with the vCard (RFC 2426 3.6.2) */
    val note: NoteProperty? = null,
    /** URLs that may be used to obtain real-time information about the object (RFC 2426 3.6.8) */
    val urls: List<UrlProperty> = emptyList(),
    /** The date of birth of the individual associated with the vCard (RFC 2426 3.1.5) */
    val birthday: BirthdayProperty? = null,
    /** The text-based nicknames of the object (RFC 2426 3.1.3) */
    val nicknames: List<NicknameProperty> = emptyList(),
    /** An image or photograph of the object (RFC 2426 3.1.4) */
    val photo: PhotoProperty? = null,
    /** The time zones of the object (RFC 2426 3.4.1) */
    val timezones: List<TimezoneProperty> = emptyList(),
    /** The geographic positions of the object (RFC 2426 3.4.2) */
    val geographicPositions: List<GeographicPositionProperty> = emptyList(),
    /** The roles, occupations, or business categories of the object (RFC 2426 3.5.2) */
    val roles: List<RoleProperty> = emptyList(),
    /** The logos of the organization associated with the object (RFC 2426 3.5.3) */
    val logos: List<LogoProperty> = emptyList(),
    /** The application categories that the object belongs to (RFC 2426 3.6.1) */
    val categories: List<CategoriesProperty> = emptyList(),
    /** The identifier for the product that created the vCard (RFC 2426 3.6.3) */
    val productIdentifier: ProductIdentifierProperty? = null,
    /** The revision date and time when the vCard was last updated (RFC 2426 3.6.4) */
    val revision: RevisionProperty? = null,
    /** Digital sound content that annotates some aspect of the object (RFC 2426 3.6.5) */
    val sounds: List<SoundProperty> = emptyList(),
    /** A value that represents a globally unique identifier corresponding to the entity (RFC 2426 3.6.7) */
    val uniqueIdentifier: UniqueIdentifierProperty? = null,
    /** The public keys or certificates associated with the object (RFC 2426 3.7.1) */
    val keys: List<KeyProperty> = emptyList(),
) : VCard() {
    override val version: String = "3.0"
    override val properties: List<VCardProperty<*>> = buildList {
        add(formattedName)
        name?.let { add(it) }
        addAll(nicknames)
        photo?.let { add(it) }
        birthday?.let { add(it) }
        addAll(physicalAddresses)
        addAll(telephoneNumbers)
        addAll(emailAddresses)
        addAll(timezones)
        addAll(geographicPositions)
        title?.let { add(it) }
        addAll(roles)
        addAll(logos)
        organization?.let { add(it) }
        addAll(categories)
        note?.let { add(it) }
        productIdentifier?.let { add(it) }
        revision?.let { add(it) }
        addAll(sounds)
        uniqueIdentifier?.let { add(it) }
        addAll(urls)
        addAll(keys)
    }
}

/**
 * Represents a vCard 2.1 object.
 */
data class V2VCard(
    /** The formatted name string associated with the vCard object (vCard 2.1) */
    val formattedName: FormattedNameProperty? = null,
    /** The components of the name of the object (vCard 2.1) */
    val name: NameProperty? = null,
    /** The electronic mail addresses for communication with the object (vCard 2.1) */
    val emailAddresses: List<EmailProperty> = emptyList(),
    /** The telephone numbers for communication with the object (vCard 2.1) */
    val telephoneNumbers: List<TelephoneProperty> = emptyList(),
    /** The delivery addresses for the object (vCard 2.1) */
    val physicalAddresses: List<AddressProperty> = emptyList(),
    /** The name and units of the organization associated with the object (vCard 2.1) */
    val organization: OrganizationProperty? = null,
    /** The organizational title or position of the object (vCard 2.1) */
    val title: TitleProperty? = null,
    /** Supplemental information or a comment that is associated with the vCard (vCard 2.1) */
    val note: NoteProperty? = null,
    /** URLs that may be used to obtain real-time information about the object (vCard 2.1) */
    val urls: List<UrlProperty> = emptyList(),
    /** The date of birth of the individual associated with the vCard (vCard 2.1) */
    val birthday: BirthdayProperty? = null,
    /** The text-based nicknames of the object (vCard 2.1) */
    val nicknames: List<NicknameProperty> = emptyList(),
    /** An image or photograph of the object (vCard 2.1) */
    val photo: PhotoProperty? = null,
    /** The time zones of the object (vCard 2.1) */
    val timezones: List<TimezoneProperty> = emptyList(),
    /** The geographic positions of the object (vCard 2.1) */
    val geographicPositions: List<GeographicPositionProperty> = emptyList(),
    /** The roles, occupations, or business categories of the object (vCard 2.1) */
    val roles: List<RoleProperty> = emptyList(),
    /** The logos of the organization associated with the object (vCard 2.1) */
    val logos: List<LogoProperty> = emptyList(),
    /** The revision date and time when the vCard was last updated (vCard 2.1) */
    val revision: RevisionProperty? = null,
    /** Digital sound content that annotates some aspect of the object (vCard 2.1) */
    val sounds: List<SoundProperty> = emptyList(),
    /** A value that represents a globally unique identifier corresponding to the entity (vCard 2.1) */
    val uniqueIdentifier: UniqueIdentifierProperty? = null,
    /** The public keys or certificates associated with the object (vCard 2.1) */
    val keys: List<KeyProperty> = emptyList(),
) : VCard() {
    override val version: String = "2.1"
    override val properties: List<VCardProperty<*>> = buildList {
        formattedName?.let { add(it) }
        name?.let { add(it) }
        addAll(nicknames)
        photo?.let { add(it) }
        birthday?.let { add(it) }
        addAll(physicalAddresses)
        addAll(telephoneNumbers)
        addAll(emailAddresses)
        addAll(timezones)
        addAll(geographicPositions)
        title?.let { add(it) }
        addAll(roles)
        addAll(logos)
        organization?.let { add(it) }
        note?.let { add(it) }
        revision?.let { add(it) }
        addAll(sounds)
        uniqueIdentifier?.let { add(it) }
        addAll(urls)
        addAll(keys)
    }
}
