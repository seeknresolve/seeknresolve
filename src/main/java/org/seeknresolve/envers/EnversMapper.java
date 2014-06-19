package org.seeknresolve.envers;

import com.google.common.base.Strings;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.seeknresolve.domain.dto.RevisionDiffDTO;
import org.seeknresolve.envers.fieldMapper.FieldMapper;
import org.springframework.data.history.Revision;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.util.*;

public abstract class EnversMapper<T> {

    private Map<Class<?>, FieldMapper> fieldMappers = new HashMap<>();

    public List<RevisionDiffDTO> diff(List<Revision<Integer, T>> revisions) throws IntrospectionException, IllegalAccessException {
        List<RevisionDiffDTO> toReturn = new ArrayList<>();
        if(revisions.size() < 2) {
            return toReturn;
        }

        Iterator<Revision<Integer, T>> iterator = revisions.iterator();
        Revision<Integer, T> after = iterator.next();
        while(iterator.hasNext()) {
            Revision<Integer, T> before = after;
            after = iterator.next();

            String description = buildDescription(before.getEntity(), after.getEntity());
            if(!Strings.isNullOrEmpty(description)) {
                toReturn.add(new RevisionDiffDTO(after.getRevisionDate(), description));
            }
        }

        return toReturn;
    }

    protected final void registerFieldMapper(Class<?> clazz, FieldMapper fieldMapper) {
        fieldMappers.put(clazz, fieldMapper);
    }

    private String buildDescription(T before, T after) throws IntrospectionException, IllegalAccessException {
        StringBuilder description = new StringBuilder();

        Class<?> clazz = before.getClass();
        do {
            getDeclaredFieldsDescription(before, after, description, clazz);
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class);

        return description.toString();
    }

    private void getDeclaredFieldsDescription(T before, T after, StringBuilder description, Class<?> clazz) throws IllegalAccessException {
        for (Field field : clazz.getDeclaredFields()) {
            if ((isAudited(field) || isAudited(clazz) && !isNotAudited(field))) {
                String fieldDesc = getDescriptionForField(before, after,field);
                if(!Strings.isNullOrEmpty(fieldDesc)) {
                    description.append(fieldDesc).append("\n");
                }
            }
        }
    }

    private String getDescriptionForField(T before, T after, Field field) throws IllegalAccessException {
        FieldMapper fieldMapper = findFieldMapper(field);
        if (fieldMapper != null) {
            field.setAccessible(true);
            if((field.get(before) != null && !field.get(before).equals(field.get(after)))
                || (field.get(after) != null && !field.get(after).equals(field.get(before)))) {
                return fieldMapper.getDescription(field.get(before), field.get(after), field.getName());
            }
        }
        return "";
    }

    private FieldMapper findFieldMapper(Field field) {
        return fieldMappers.get(field.getType());
    }

    private boolean isAudited(Class<?> clazz) {
        return clazz.getAnnotation(Audited.class) != null;
    }

    private boolean isAudited(Field field) {
        return field.getAnnotation(Audited.class) != null;
    }

    private boolean isNotAudited(Field field) {
        return field.getAnnotation(NotAudited.class) != null;
    }

}
