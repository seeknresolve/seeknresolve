-- liquibase formatted sql
-- changeset rnw:add_history_view_permission

insert into permission(permission_name) values('user:view_history');

insert into role_permissions(role_roleName, permissions_permissionName) values('ADMIN', 'user:view_history');
insert into role_permissions(role_roleName, permissions_permissionName) values('PM', 'user:view_history');
