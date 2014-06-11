package pl.edu.pw.ii.pik01.seeknresolve.envers;

import org.hibernate.envers.Audited;
import org.springframework.data.history.Revision;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.RevisionDiffDTO;
import pl.edu.pw.ii.pik01.seeknresolve.envers.fieldMapper.FieldMapper;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.util.*;

public abstract class EnversMapper<T, N extends Number & Comparable<N>> {

    private Map<Class<?>, FieldMapper> fieldMappers = new HashMap<>();

    public List<RevisionDiffDTO> diff(List<Revision<N, T>> revisions) throws IntrospectionException, IllegalAccessException {
        List<RevisionDiffDTO> toReturn = new ArrayList<>();
        if(revisions.size() < 2) {
            return toReturn;
        }

        Iterator<Revision<N, T>> iterator = revisions.iterator();
        Revision<N, T> after = iterator.next();
        while(iterator.hasNext()) {
            Revision<N, T> before = after;
            after = iterator.next();

            String description = buildDescription(before.getEntity(), after.getEntity());
            toReturn.add(new RevisionDiffDTO(after.getRevisionDate(), description));
        }

        return toReturn;
    }

    protected final void registerFieldMapper(Class<?> clazz, FieldMapper fieldMapper) {
        fieldMappers.put(clazz, fieldMapper);
    }

    private String buildDescription(T earlier, T later) throws IntrospectionException, IllegalAccessException {
        StringBuilder description = new StringBuilder();

        Class<?> clazz = earlier.getClass();
        do {
            if (isAudited(clazz)) {
                getDeclaredFieldsDescription(earlier, later, description, clazz);
            }
        } while (clazz != Object.class);

        return description.toString();
    }

    private void getDeclaredFieldsDescription(T before, T after, StringBuilder description, Class<?> clazz) throws IllegalAccessException {
        for (Field field : clazz.getDeclaredFields()) {
            if (isAudited(field)) {
                getDescriptionForField(before, after, description, field);
            }
        }
    }

    private void getDescriptionForField(T before, T after, StringBuilder description, Field field) throws IllegalAccessException {
        FieldMapper fieldMapper = findFieldMapper(field);
        if (fieldMapper != null) {
            field.setAccessible(true);
            description.append(fieldMapper.getDescription(field.get(before), field.get(after), field.getName()));
        }
    }

    private FieldMapper findFieldMapper(Field field) {
        return null;
    }

    private boolean isAudited(Class<?> clazz) {
        return clazz.getAnnotation(Audited.class) != null;
    }

    private boolean isAudited(Field field) {
        return field.getAnnotation(Audited.class) != null;
    }

}
