package org.seeknresolve.envers.fieldMapper;

public abstract class AbstractFieldMapper<T> implements FieldMapper {

    @Override
    public final String getDescription(Object before, Object after, String fieldName) {
        return doGetDescription((T) before, (T) after, fieldName);
    }

    protected abstract String doGetDescription(T before, T after, String fieldName);

    protected String getFormattedDesc(String before, String after, String fieldName) {
        return new StringBuilder(formatFieldName(fieldName))
                .append(": ")
                .append(before)
                .append(" -> ")
                .append(after)
                .toString();
    }

    protected String formatEnumValue(String enumValue) {
        StringBuilder sb = new StringBuilder(enumValue.replace("_", " ").toLowerCase());
        sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
        return sb.toString();
    }

    protected String formatFieldName(String fieldName) {
        StringBuilder sb = new StringBuilder(fieldName.replaceAll("(.)([A-Z])", "$1 $2").toLowerCase());
        sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
        return sb.toString();
    }
}
