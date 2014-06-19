package org.seeknresolve.envers.fieldMapper;

import org.seeknresolve.domain.entity.Bug;

public class PriorityMapper extends AbstractFieldMapper<Bug.Priority>{
    @Override
    protected String doGetDescription(Bug.Priority before, Bug.Priority after, String fieldName) {
        String beforeFormatted = before == null ? "" : formatEnumValue(before.toString());
        String afterFormatted = after == null ? "" : formatEnumValue(after.toString());
        return getFormattedDesc(beforeFormatted, afterFormatted, fieldName);
    }
}
