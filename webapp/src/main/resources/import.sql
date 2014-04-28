insert into user(id, login, password, first_name, last_name, email) values(0, 'rnw', '', 'Rafał', 'Nowak', 'rafal@seeknresolve.pl');
insert into user(id, login, password, first_name, last_name, email) values(1, 'wkr', '', 'Wojciech', 'Korzeniowski', 'wojtek@seeknresolve.pl');
insert into user(id, login, password, first_name, last_name, email) values(2, 'mdz', '', 'Mateusz', 'Dziurdziak', 'mateusz@seeknresolve.pl');
insert into user(id, login, password, first_name, last_name, email) values(3, 'ask', '', 'Alicja', 'Skóra', 'alicja@seeknresolve.pl');
insert into user(id, login, password, first_name, last_name, email) values(4, 'sts', '', 'Stefan', 'Testowy', 'stefan@seeknresolve.pl');

insert into project(id, name, description, date_created) values(0, 'SeekNResolve', '', CURRENT_TIMESTAMP());
insert into project(id, name, description, date_created) values(1, 'WebTeX', '', CURRENT_TIMESTAMP());
insert into project(id, name, description, date_created) values(2, 'Test', '', CURRENT_TIMESTAMP());

insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-1', 'Bug 1', '', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 4, 1, 0, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-2', 'Bug 2', '', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 4, 0, 0, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-3', 'Bug 3', '', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 4, 2, 0, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('WBT-1', 'Bug 1', '', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3, 3, 1, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('WBT-2', 'Bug 2', '', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 4, 3, 1, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('TTT-1', 'Bug 1', '', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2, 0, 2, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('TTT-2', 'Bug 2', '', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1, 1, 2, 'NEW', 'NORMAL');