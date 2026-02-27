package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.HasMediatype
import com.sebastianvm.contacts.vcard.parameters.HasType
import com.sebastianvm.contacts.vcard.parameters.MediatypeParameter
import com.sebastianvm.contacts.vcard.parameters.PidParameter
import com.sebastianvm.contacts.vcard.parameters.PrefParameter
import com.sebastianvm.contacts.vcard.parameters.TypeParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents the URIs for the entity's calendar.
 *
 * This property is based on the semantics of the RFC 6350 6.9.3 (vCard 4.0).
 *
 * Example: `CALURI;MEDIATYPE=text/calendar:https://ftp.example.com/calendars/jdoe.ics`
 *
 * @property value The calendar URI.
 */
data class CalendarUriProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
    override val mediatype: MediatypeParameter? = null,
    val pid: PidParameter? = null,
    val pref: PrefParameter? = null,
    override val type: TypeParameter? = null,
    val altId: AltIdParameter? = null,
) : StringVCardProperty, HasType, HasMediatype {
    override val name: String = "CALURI"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, mediatype, pid, pref, type, altId)
}
