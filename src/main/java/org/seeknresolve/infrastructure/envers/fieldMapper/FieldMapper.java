package org.seeknresolve.infrastructure.envers.fieldMapper;

public interface FieldMapper {

    String getDescription(Object beforeValue, Object afterValue, String fieldName);

}
