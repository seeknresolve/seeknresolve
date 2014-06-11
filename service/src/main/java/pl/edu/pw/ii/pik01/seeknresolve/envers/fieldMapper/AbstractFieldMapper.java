package pl.edu.pw.ii.pik01.seeknresolve.envers.fieldMapper;

public abstract class AbstractFieldMapper<T> implements FieldMapper<T> {

    @Override
    public final String getDescription(Object before, Object after, String fieldName) {
        return getDescription((T) before, (T) after, fieldName);
    }

    protected abstract String doGetDescription(T before, T after, String fieldName);

    protected String getFormattedDesc(String before, String after, String fieldName) {
        return new StringBuilder(fieldName)
                .append(": ")
                .append(before)
                .append(" -> ")
                .append(after)
                    .toString();
    }
}
