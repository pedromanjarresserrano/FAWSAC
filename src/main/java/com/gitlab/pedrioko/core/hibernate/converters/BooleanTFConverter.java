package com.gitlab.pedrioko.core.hibernate.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * The Class BooleanTFConverter.
 */
@Converter(autoApply = true)
public class BooleanTFConverter implements AttributeConverter<Boolean, String> {

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.
     * Object)
     */
    @Override
    public String convertToDatabaseColumn(Boolean value) {
        if (Boolean.TRUE.equals(value)) {
            return "T";
        } else {
            return "F";
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.persistence.AttributeConverter#convertToEntityAttribute(java.lang.
     * Object)
     */
    @Override
    public Boolean convertToEntityAttribute(String value) {
        return "T".equals(value);
    }
}