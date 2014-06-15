-- liquibase formatted sql
-- changeset rnw:sample_user_data

insert into user_role(role_name) values('ADMIN');
insert into user_role(role_name) values('USER');

insert into "user"(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked, date_created, date_modified) values(0, 'rnw', '$2a$10$pLsk8I4mzjLt.7YFfFaw2OyAC8GWR8pTwbvZbd7g4f3QFtRvViSVC', 'Rafał', 'Nowak', 'rafal@seeknresolve.pl', 'USER', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into "user"(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked, date_created, date_modified) values(1, 'wkr', '$2a$10$UFSe2z2hETbNNkx/HkUf6e2Em4zKAlKj.c0eY0JvzVQZb.1X4iumy', 'Wojciech', 'Korzeniowski', 'wojtek@seeknresolve.pl', 'USER', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into "user"(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked, date_created, date_modified) values(2, 'mdz', '$2a$10$9f4hCQb0j2C2s4L4Xvd/WOEsOF7IkrwSzgIZEFJYE4Wl.oWR.hxu6', 'Mateusz', 'Dziurdziak', 'mateusz@seeknresolve.pl', 'USER', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into "user"(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked, date_created, date_modified) values(3, 'ask', '$2a$10$3GHWgCUth27MaN6n2FrDg.Os0C9zmvCJtb1cDZ/8KgZJRgFHmLQ02', 'Alicja', 'Skóra', 'alicja@seeknresolve.pl', 'USER', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into "user"(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked, date_created, date_modified) values(4, 'sts', '$2a$10$Tb4pqBJIYSAdzKwDtJg1u.Nk06WU7rpbm12wkuwPy6MnMYgMYfq6q', 'Stefan', 'Testowy', 'stefan@seeknresolve.pl', 'USER', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into "user"(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked, date_created, date_modified) values(5, 'admin', '$2a$10$r1Kc2e35BquGDCeefipKa.RWTiLyNa9tKnrLSlEk1JqlFkxNQtzhy', 'ADMIN', 'ADMIN', 'admin@admin.admin', 'ADMIN', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

insert into project_role(role_name) values('PM');
insert into project_role(role_name) values('DEVELOPER');
insert into project_role(role_name) values('EXTERNAL');
insert into project_role(role_name) values('GUEST');

insert into permission(permission_name) values('admin:everything');
insert into permission(permission_name) values('project:everything');
insert into permission(permission_name) values('project:view');
insert into permission(permission_name) values('project:add_bug');
insert into permission(permission_name) values('project:edit_bug');
insert into permission(permission_name) values('project:add_member');
insert into permission(permission_name) values('project:delete');
insert into permission(permission_name) values('project:update');
insert into permission(permission_name) values('project:create');
insert into permission(permission_name) values('permission:create');
insert into permission(permission_name) values('permission:delete');
insert into permission(permission_name) values('user:create');
insert into permission(permission_name) values('user:update');
insert into permission(permission_name) values('user:change_password');
insert into permission(permission_name) values('role:add_permission');
insert into permission(permission_name) values('role:delete_permission');

insert into role_permissions(role_roleName, permissions_permissionName) values('ADMIN', 'admin:everything');
insert into role_permissions(role_roleName, permissions_permissionName) values('ADMIN', 'permission:create');
insert into role_permissions(role_roleName, permissions_permissionName) values('ADMIN', 'permission:delete');
insert into role_permissions(role_roleName, permissions_permissionName) values('ADMIN', 'user:create');
insert into role_permissions(role_roleName, permissions_permissionName) values('ADMIN', 'user:update');
insert into role_permissions(role_roleName, permissions_permissionName) values('ADMIN', 'user:change_password');
insert into role_permissions(role_roleName, permissions_permissionName) values('ADMIN', 'role:add_permission');
insert into role_permissions(role_roleName, permissions_permissionName) values('ADMIN', 'role:delete_permission');
insert into role_permissions(role_roleName, permissions_permissionName) values('PM', 'project:everything');
insert into role_permissions(role_roleName, permissions_permissionName) values('DEVELOPER', 'project:view');
insert into role_permissions(role_roleName, permissions_permissionName) values('DEVELOPER', 'project:add_bug');
insert into role_permissions(role_roleName, permissions_permissionName) values('DEVELOPER', 'project:edit_bug');
insert into role_permissions(role_roleName, permissions_permissionName) values('DEVELOPER', 'project:delete');
insert into role_permissions(role_roleName, permissions_permissionName) values('DEVELOPER', 'project:update');
insert into role_permissions(role_roleName, permissions_permissionName) values('DEVELOPER', 'project:create');
insert into role_permissions(role_roleName, permissions_permissionName) values('EXTERNAL', 'project:everything');
insert into role_permissions(role_roleName, permissions_permissionName) values('EXTERNAL', 'project:view');
insert into role_permissions(role_roleName, permissions_permissionName) values('EXTERNAL', 'project:add_bug');
insert into role_permissions(role_roleName, permissions_permissionName) values('GUEST', 'project:view');
