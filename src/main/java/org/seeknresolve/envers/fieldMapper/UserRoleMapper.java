package org.seeknresolve.envers.fieldMapper;


import org.seeknresolve.domain.entity.UserRole;

public class UserRoleMapper extends AbstractFieldMapper<UserRole> {
    @Override
    public String doGetDescription(UserRole beforeValue, UserRole afterValue, String fieldName) {
        String beforeFormatted = beforeValue == null ? "" : beforeValue.getRoleName().toString();
        String afterFormatted = afterValue == null ? "" : afterValue.getRoleName().toString();
        return getFormattedDesc(beforeFormatted, afterFormatted, fieldName);
    }
}
