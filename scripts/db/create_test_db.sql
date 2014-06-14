drop database if exists "seeknresolve-test";
drop user if exists "seeknresolve-test";

create user "seeknresolve-test" password 'seeknresolve-test';

create database "seeknresolve-test";
alter database "seeknresolve-test" owner to "seeknresolve-test";
