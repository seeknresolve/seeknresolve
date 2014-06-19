package org.seeknresolve.envers.fieldMapper;

public interface FieldMapper {

    String getDescription(Object beforeValue, Object afterValue, String fieldName);

}
