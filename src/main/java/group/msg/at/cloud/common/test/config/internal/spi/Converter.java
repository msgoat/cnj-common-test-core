package group.msg.at.cloud.common.test.config.internal.spi;

public interface Converter<T> {
    /**
     * Configure the string value to a specified type
     *
     * @param value the string representation of a property value.
     * @return the converted value or null
     * @throws IllegalArgumentException if the value cannot be converted to the specified type.
     */
    T convert(String value);
}