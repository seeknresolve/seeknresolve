insert into user_role(role_name) values('ADMIN');
insert into user_role(role_name) values('USER');

insert into user(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked) values(0, 'rnw', 'rnw', 'Rafał', 'Nowak', 'rafal@seeknresolve.pl', 'USER', 1, 0, 0);
insert into user(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked) values(1, 'wkr', 'wkr', 'Wojciech', 'Korzeniowski', 'wojtek@seeknresolve.pl', 'USER', 1, 0, 0);
insert into user(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked) values(2, 'mdz', 'mdz', 'Mateusz', 'Dziurdziak', 'mateusz@seeknresolve.pl', 'USER', 1, 0, 0);
insert into user(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked) values(3, 'ask', 'ask', 'Alicja', 'Skóra', 'alicja@seeknresolve.pl', 'USER', 1, 0, 0);
insert into user(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked) values(4, 'sts', 'sts', 'Stefan', 'Testowy', 'stefan@seeknresolve.pl', 'USER', 1, 0, 0);
insert into user(id, login, password, first_name, last_name, email, user_role, enabled, expired, locked) values(5, 'admin', 'admin', 'ADMIN', 'ADMIN', 'admin@admin.admin', 'ADMIN', 1, 0, 0);

insert into project(id, name, description, date_created) values(0, 'SeekNResolve', 'Szajs robiony dla zaliczenia przedmiotu', CURRENT_TIMESTAMP());
insert into project(id, name, description, date_created) values(1, 'WebTeX', 'Jedyny webowy edytor LaTeX w internetach', CURRENT_TIMESTAMP());
insert into project(id, name, description, date_created) values(2, 'Test', 'Testowy opis testowego projetu', CURRENT_TIMESTAMP());

insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-1', 'Bug 1', 'Opis buga', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 4, 1, 0, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-2', 'Bug 2', 'Opis buga', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 4, 0, 0, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-3', 'Bug 3', 'Opis buga', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 4, 2, 0, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('WBT-1', 'Bug 1', 'Opis buga', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3, 3, 1, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('WBT-2', 'Bug 2', 'Opis buga', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 4, 3, 1, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('TTT-1', 'Bug 1', 'Opis buga', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2, 0, 2, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('TTT-2', 'Bug 2', 'Opis buga', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1, 1, 2, 'NEW', 'NORMAL');

insert into project_role(role_name) values('PM');
insert into project_role(role_name) values('DEVELOPER');
insert into project_role(role_name) values('EXTERNAL');
insert into project_role(role_name) values('GUEST');

insert into permission(id, permission) values(0, 'ADMIN_CAN_EVERYTHING');
insert into permission(id, permission) values(1, 'PROJECT_CAN_EVERYTHING');
insert into permission(id, permission) values(2, 'PROJECT_CAN_SEE');
insert into permission(id, permission) values(3, 'PROJECT_CAN_ADD_BUG');
insert into permission(id, permission) values(4, 'PROJECT_CAN_EDIT_BUG');
insert into permission(id, permission) values(5, 'PROJECT_CAN_ADD_MEMBER');

insert into role_permissions(roles_roleName, permissions_id) values('ADMIN', 0);
insert into role_permissions(roles_roleName, permissions_id) values('PM', 1);
insert into role_permissions(roles_roleName, permissions_id) values('DEVELOPER', 2);
insert into role_permissions(roles_roleName, permissions_id) values('DEVELOPER', 3);
insert into role_permissions(roles_roleName, permissions_id) values('DEVELOPER', 4);
insert into role_permissions(roles_roleName, permissions_id) values('DEVELOPER', 5);
insert into role_permissions(roles_roleName, permissions_id) values('EXTERNAL', 2);
insert into role_permissions(roles_roleName, permissions_id) values('EXTERNAL', 3);
insert into role_permissions(roles_roleName, permissions_id) values('EXTERNAL', 4);
insert into role_permissions(roles_roleName, permissions_id) values('GUEST', 2);

insert into user_project_role(id, project_id, project_role_roleName, user_id) values(0, 0, 'PM', 0);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(1, 1, 'PM', 0);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(2, 0, 'PM', 1);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(3, 0, 'PM', 2);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(4, 0, 'GUEST', 3);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(5, 1, 'GUEST', 3);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(6, 2, 'GUEST', 3);
