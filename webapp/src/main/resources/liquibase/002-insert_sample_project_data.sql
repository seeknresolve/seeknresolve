-- liquibase formatted sql
-- changeset rnw:sample_project_data

insert into project(id, name, tag, description, date_created, date_modified, last_bug_number) values(0, 'SeekNResolve', 'SNR', 'Program robiony dla zaliczenia przedmiotu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3);
insert into project(id, name, tag, description, date_created, date_modified, last_bug_number) values(1, 'WebTeX', 'WBTX', 'Jedyny webowy edytor LaTeX w internetach', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);
insert into project(id, name, tag, description, date_created, date_modified, last_bug_number) values(2, 'Test', 'TTT', 'Testowy opis testowego projetu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);

insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-1', 'Bug 1', 'Opis buga', to_date('2014-06-10 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-11 01:30:15', 'YYYY-MM-DD HH24:MI:SS'), 0, 1, 0, 'IN_PROGRESS', 'LOW');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-2', 'Bug 2', 'Opis buga', to_date('2014-06-11 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-15 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 4, 0, 0, 'IN_PROGRESS', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-3', 'Bug 3', 'Opis buga', to_date('2014-06-12 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-13 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 4, 2, 0, 'CLOSED', 'HIGH');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-4', 'Bug 3', 'Opis buga', to_date('2014-06-13 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-14 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 4, 2, 0, 'READY_TO_TEST', 'HIGH');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-5', 'Bug 3', 'Opis buga', to_date('2014-06-14 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-18 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 4, 2, 0, 'CLOSED', 'HIGH');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-6', 'Bug 3', 'Opis buga', to_date('2014-06-15 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-16 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 4, 2, 0, 'REOPENED', 'HIGH');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-7', 'Bug 3', 'Opis buga', to_date('2014-06-15 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-18 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 4, 2, 0, 'IN_PROGRESS', 'HIGH');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-8', 'Bug 3', 'Opis buga', to_date('2014-06-16 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-19 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 4, 2, 0, 'READY_TO_TEST', 'HIGH');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-9', 'Bug 3', 'Opis buga', to_date('2014-06-13 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-16 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 4, 2, 0, 'IN_PROGRESS', 'HIGH');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-10', 'Bug 3', 'Opis buga', to_date('2014-06-13 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-13 23:30:15', 'YYYY-MM-DD HH24:MI:SS'), 4, 2, 0, 'REOPENED', 'HIGH');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-11', 'Bug 3', 'Opis buga', to_date('2014-06-14 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-16 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 4, 2, 0, 'READY_TO_TEST', 'HIGH');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-12', 'Bug 3', 'Opis buga', to_date('2014-06-16 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-17 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 4, 2, 0, 'CLOSED', 'HIGH');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-13', 'Bug 3', 'Opis buga', to_date('2014-06-16 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-20 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 4, 2, 0, 'READY_TO_TEST', 'HIGH');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('SNR-14', 'Bug 3', 'Opis buga', to_date('2014-06-11 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-12 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 4, 2, 0, 'CLOSED', 'HIGH');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('WBTX-1', 'Bug 1', 'Opis buga', to_date('2014-06-10 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-10 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 3, 3, 1, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('WBTX-2', 'Bug 2', 'Opis buga', to_date('2014-06-10 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-10 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 4, 3, 1, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('TTT-1', 'Bug 1', 'Opis buga', to_date('2014-06-10 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-10 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 2, 0, 2, 'NEW', 'NORMAL');
insert into bug(tag, name, description, date_created, date_modified, reporter_id, assignee_id, project_id, state, priority) values('TTT-2', 'Bug 2', 'Opis buga', to_date('2014-06-10 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), to_date('2014-06-10 21:30:15', 'YYYY-MM-DD HH24:MI:SS'), 1, 1, 2, 'NEW', 'NORMAL');

insert into comment(id, content, date_created, author_id, bug_tag) values(0, 'Bardzo ladny bug. Daje okeje.', CURRENT_TIMESTAMP, 1, 'SNR-1');
insert into comment(id, content, date_created, author_id, bug_tag) values(1, 'Ooo, faktycznie, ale ladny.', CURRENT_TIMESTAMP, 0, 'SNR-1');
insert into comment(id, content, date_created, author_id, bug_tag) values(2, 'Kto to takie bugi robi ja sie pytam?!', CURRENT_TIMESTAMP, 1, 'SNR-2');
insert into comment(id, content, date_created, author_id, bug_tag) values(3, 'Polecam, Jan Zamoyski.', CURRENT_TIMESTAMP, 2, 'WBTX-1');

insert into user_project_role(id, project_id, project_role_roleName, user_id) values(0, 0, 'PM', 0);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(1, 1, 'PM', 0);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(2, 0, 'PM', 1);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(3, 0, 'PM', 2);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(4, 0, 'GUEST', 3);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(5, 1, 'GUEST', 3);
insert into user_project_role(id, project_id, project_role_roleName, user_id) values(6, 2, 'GUEST', 3);
