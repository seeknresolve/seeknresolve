package pl.edu.pw.ii.pik01.seeknresolve.envers.fieldMapper;

public interface FieldMapper {

    String getDescription(Object beforeValue, Object afterValue, String fieldName);

}
