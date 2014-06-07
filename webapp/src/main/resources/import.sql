insert into user_role(role_name) values('ADMIN');
insert into user_role(role_name) values('USER');

insert into user(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked) values(0, 'rnw', '$2a$10$pLsk8I4mzjLt.7YFfFaw2OyAC8GWR8pTwbvZbd7g4f3QFtRvViSVC', 'Rafał', 'Nowak', 'rafal@seeknresolve.pl', 'USER', 1, 0, 0);
insert into user(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked) values(1, 'wkr', '$2a$10$UFSe2z2hETbNNkx/HkUf6e2Em4zKAlKj.c0eY0JvzVQZb.1X4iumy', 'Wojciech', 'Korzeniowski', 'wojtek@seeknresolve.pl', 'USER', 1, 0, 0);
insert into user(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked) values(2, 'mdz', '$2a$10$9f4hCQb0j2C2s4L4Xvd/WOEsOF7IkrwSzgIZEFJYE4Wl.oWR.hxu6', 'Mateusz', 'Dziurdziak', 'mateusz@seeknresolve.pl', 'USER', 1, 0, 0);
insert into user(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked) values(3, 'ask', '$2a$10$3GHWgCUth27MaN6n2FrDg.Os0C9zmvCJtb1cDZ/8KgZJRgFHmLQ02', 'Alicja', 'Skóra', 'alicja@seeknresolve.pl', 'USER', 1, 0, 0);
insert into user(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked) values(4, 'sts', '$2a$10$Tb4pqBJIYSAdzKwDtJg1u.Nk06WU7rpbm12wkuwPy6MnMYgMYfq6q', 'Stefan', 'Testowy', 'stefan@seeknresolve.pl', 'USER', 1, 0, 0);
insert into user(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked) values(5, 'admin', '$2a$10$r1Kc2e35BquGDCeefipKa.RWTiLyNa9tKnrLSlEk1JqlFkxNQtzhy', 'ADMIN', 'ADMIN', 'admin@admin.admin', 'ADMIN', 1, 0, 0);

insert into project(id, name, description, date_created) values(0, 'SeekNResolve', 'Szajs robiony dla zaliczenia przedmiotu', CURRENT_TIMESTAMP());
insert into project(id, name, description, date_created) values(1, 'WebTeX', 'Jedyny webowy edytor LaTeX w internetach', CURRENT_TIMESTAMP());
insert into project(id, name, description, date_created) values(2, 'Test', 'Testowy opis testowego projetu', CURRENT_TIMESTAMP());

insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-1', 'Bug 1', 'Opis buga', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 4, 1, 0, 'NEW', 'LOW');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-2', 'Bug 2', 'Opis buga', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 4, 0, 0, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-3', 'Bug 3', 'Opis buga', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 4, 2, 0, 'NEW', 'HIGH');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('WBT-1', 'Bug 1', 'Opis buga', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3, 3, 1, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('WBT-2', 'Bug 2', 'Opis buga', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 4, 3, 1, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('TTT-1', 'Bug 1', 'Opis buga', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2, 0, 2, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('TTT-2', 'Bug 2', 'Opis buga', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1, 1, 2, 'NEW', 'NORMAL');

insert into comment(id, content, date_created, author_id, bug_tag) values(0, 'Bardzo ladny bug. Daje okeje.', CURRENT_TIMESTAMP(), 1, 'SNR-1');
insert into comment(id, content, date_created, author_id, bug_tag) values(1, 'Ooo, faktycznie, ale ladny.', CURRENT_TIMESTAMP(), 0, 'SNR-1');
insert into comment(id, content, date_created, author_id, bug_tag) values(2, 'Kto to takie bugi robi ja sie pytam?!', CURRENT_TIMESTAMP(), 1, 'SNR-2');
insert into comment(id, content, date_created, author_id, bug_tag) values(3, 'Polecam, Jan Zamoyski.', CURRENT_TIMESTAMP(), 2, 'WBT-1');

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
insert into permission(permission_name) values('user:create');
insert into permission(permission_name) values('user:update');
insert into permission(permission_name) values('role:add_permission');
insert into permission(permission_name) values('role:delete_permission');

insert into role_permissions(role_roleName, permissions_permissionName) values('ADMIN', 'admin:everything');
insert into role_permissions(role_roleName, permissions_permissionName) values('ADMIN', 'permission:create');
insert into role_permissions(role_roleName, permissions_permissionName) values('ADMIN', 'user:create');
insert into role_permissions(role_roleName, permissions_permissionName) values('ADMIN', 'user:update');
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

insert into user_project_role(id, project_id, project_role_roleName, user_id) values(0, 0, 'PM', 0);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(1, 1, 'PM', 0);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(2, 0, 'PM', 1);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(3, 0, 'PM', 2);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(4, 0, 'GUEST', 3);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(5, 1, 'GUEST', 3);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(6, 2, 'GUEST', 3);
