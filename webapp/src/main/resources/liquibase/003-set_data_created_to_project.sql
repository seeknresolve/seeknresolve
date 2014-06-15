-- liquibase formatted sql
-- changeset wkr:update_sample_projects
update project
set date_created=to_date('2014-06-5 21:30:15', 'YYYY-MM-DD HH24:MI:SS')
where id=0;

update project
set date_created=to_date('2014-06-14 21:30:15', 'YYYY-MM-DD HH24:MI:SS')
where id=1;

update project
set date_created=to_date('2014-06-15 21:30:15', 'YYYY-MM-DD HH24:MI:SS')
where id=2;

