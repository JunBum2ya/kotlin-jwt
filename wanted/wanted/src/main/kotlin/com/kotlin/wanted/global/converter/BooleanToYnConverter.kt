package com.kotlin.wanted.global.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class BooleanToYnConverter : AttributeConverter<Boolean,String>{
    override fun convertToDatabaseColumn(attribute: Boolean): String {
        return if(attribute) "Y" else "N"
    }

    override fun convertToEntityAttribute(dbData: String): Boolean {
        return "Y" == dbData
    }
}