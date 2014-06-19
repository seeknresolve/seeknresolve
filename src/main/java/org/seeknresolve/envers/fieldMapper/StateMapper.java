package org.seeknresolve.envers.fieldMapper;

import org.seeknresolve.domain.entity.Bug;

public class StateMapper extends AbstractFieldMapper<Bug.State>{
    @Override
    protected String doGetDescription(Bug.State before, Bug.State after, String fieldName) {
        String beforeFormatted = before == null ? "" : formatEnumValue(before.toString());
        String afterFormatted = after == null ? "" : formatEnumValue(after.toString());
        return getFormattedDesc(beforeFormatted, afterFormatted, fieldName);
    }
}
