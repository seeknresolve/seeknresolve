package pl.edu.pw.ii.pik01.seeknresolve.envers.fieldMapper;

public interface FieldMapper<T> {

    String getDescription(T beforeValue, T afterValue, String fieldName);

}
