insert into user_role(role_name) values('ADMIN');
insert into user_role(role_name) values('USER');

insert into user(id, login, password, first_name, last_name, email, user_role) values(0, 'rnw', '', 'Rafał', 'Nowak', 'rafal@seeknresolve.pl', 'USER');
insert into user(id, login, password, first_name, last_name, email, user_role) values(1, 'wkr', '', 'Wojciech', 'Korzeniowski', 'wojtek@seeknresolve.pl', 'USER');
insert into user(id, login, password, first_name, last_name, email, user_role) values(2, 'mdz', '', 'Mateusz', 'Dziurdziak', 'mateusz@seeknresolve.pl', 'USER');
insert into user(id, login, password, first_name, last_name, email, user_role) values(3, 'ask', '', 'Alicja', 'Skóra', 'alicja@seeknresolve.pl', 'USER');
insert into user(id, login, password, first_name, last_name, email, user_role) values(4, 'sts', '', 'Stefan', 'Testowy', 'stefan@seeknresolve.pl', 'USER');
insert into user(id, login, password, first_name, last_name, email, user_role) values(5, 'admin', 'admin', 'ADMIN', 'ADMIN', 'admin@admin.admin', 'ADMIN');

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
