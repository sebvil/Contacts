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
import com.sebastianvm.contacts.vcard.parameters.Value
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

object ParameterFactory {

    fun create(parsed: ParsedParameter): VCardPropertyParameter<*>? {
        return when (parsed.name) {
            "TYPE" -> TypeParameter(parsed.value.split(","))
            "LANGUAGE" -> LanguageParameter(parsed.value)
            "PREF" -> parsed.value.toIntOrNull()?.let { PrefParameter(it) }
            "VALUE" -> {
                val v =
                    Value.entries.find { it.vCardString.equals(parsed.value, ignoreCase = true) }
                v?.let { ValueParameter(it) }
            }
            "ALTID" -> AltIdParameter(parsed.value)
            "PID" -> PidParameter(parsed.value)
            "MEDIATYPE" -> MediatypeParameter(parsed.value)
            "CALSCALE" -> CalscaleParameter(parsed.value)
            "SORT-AS" -> SortAsParameter(parsed.value.split(","))
            "GEO" -> GeoParameter(parsed.value)
            "TZ" -> TzParameter(parsed.value)
            "LABEL" -> LabelParameter(parsed.value)
            else -> null
        }
    }

    fun createAll(params: List<ParsedParameter>): List<VCardPropertyParameter<*>> =
        params.mapNotNull { create(it) }
}
