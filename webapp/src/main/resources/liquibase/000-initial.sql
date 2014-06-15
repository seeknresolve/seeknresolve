-- liquibase formatted sql
-- changeset rnw:initial

CREATE TABLE attachment (
  id bigint NOT NULL,
  description character varying(255),
  file bytea NOT NULL,
  bug_tag character varying(255) NOT NULL
);

CREATE TABLE bug (
  tag character varying(255) NOT NULL,
  date_created timestamp without time zone NOT NULL,
  date_modified timestamp without time zone NOT NULL,
  description character varying(255) NOT NULL,
  name character varying(255) NOT NULL,
  priority character varying(255),
  state character varying(255),
  assignee_id bigint,
  project_id bigint NOT NULL,
  reporter_id bigint NOT NULL
);

CREATE TABLE bug_aud (
  tag character varying(255) NOT NULL,
  rev integer NOT NULL,
  revtype smallint,
  date_created timestamp without time zone,
  date_modified timestamp without time zone,
  description character varying(255),
  name character varying(255),
  priority character varying(255),
  state character varying(255),
  assignee_id bigint,
  reporter_id bigint
);

CREATE TABLE comment (
  id bigint NOT NULL,
  content character varying(255) NOT NULL,
  date_created timestamp without time zone NOT NULL,
  author_id bigint NOT NULL,
  bug_tag character varying(255) NOT NULL
);

CREATE SEQUENCE hibernate_sequence
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE TABLE permission (
  permission_name character varying(255) NOT NULL
);

CREATE TABLE project (
  id bigint NOT NULL,
  date_created timestamp without time zone NOT NULL,
  date_modified timestamp without time zone NOT NULL,
  description character varying(255) NOT NULL,
  last_bug_number bigint NOT NULL,
  name character varying(255) NOT NULL,
  tag character varying(255) NOT NULL,
  version bigint DEFAULT 0 NOT NULL
);

CREATE TABLE project_aud (
  id bigint NOT NULL,
  rev integer NOT NULL,
  revtype smallint,
  date_created timestamp without time zone,
  date_modified timestamp without time zone,
  description character varying(255),
  name character varying(255),
  tag character varying(255)
);

CREATE TABLE project_role (
  role_name character varying(255) NOT NULL
);

CREATE TABLE revinfo (
  rev integer NOT NULL,
  revtstmp bigint
);

CREATE TABLE role_permissions (
  role_rolename character varying(255) NOT NULL,
  permissions_permissionname character varying(255) NOT NULL
);

CREATE TABLE "user" (
  id bigint NOT NULL,
  date_created timestamp without time zone NOT NULL,
  date_modified timestamp without time zone NOT NULL,
  email character varying(255) NOT NULL,
  enabled boolean NOT NULL,
  expired boolean NOT NULL,
  first_name character varying(255) NOT NULL,
  last_name character varying(255) NOT NULL,
  locked boolean NOT NULL,
  login character varying(255) NOT NULL,
  password character varying(255) NOT NULL,
  user_role character varying(255) NOT NULL
);

CREATE TABLE user_aud (
  id bigint NOT NULL,
  rev integer NOT NULL,
  revtype smallint,
  date_created timestamp without time zone,
  date_modified timestamp without time zone,
  email character varying(255),
  first_name character varying(255),
  last_name character varying(255),
  login character varying(255),
  user_role character varying(255)
);

CREATE TABLE user_project_role (
  id bigint NOT NULL,
  project_id bigint NOT NULL,
  project_role_rolename character varying(255) NOT NULL,
  user_id bigint NOT NULL
);

CREATE TABLE user_role (
  role_name character varying(255) NOT NULL
);

ALTER TABLE ONLY attachment
ADD CONSTRAINT attachment_pkey PRIMARY KEY (id);

ALTER TABLE ONLY bug_aud
ADD CONSTRAINT bug_aud_pkey PRIMARY KEY (tag, rev);

ALTER TABLE ONLY bug
ADD CONSTRAINT bug_pkey PRIMARY KEY (tag);

ALTER TABLE ONLY comment
ADD CONSTRAINT comment_pkey PRIMARY KEY (id);

ALTER TABLE ONLY permission
ADD CONSTRAINT permission_pkey PRIMARY KEY (permission_name);

ALTER TABLE ONLY project_aud
ADD CONSTRAINT project_aud_pkey PRIMARY KEY (id, rev);

ALTER TABLE ONLY project
ADD CONSTRAINT project_pkey PRIMARY KEY (id);

ALTER TABLE ONLY project_role
ADD CONSTRAINT project_role_pkey PRIMARY KEY (role_name);

ALTER TABLE ONLY revinfo
ADD CONSTRAINT revinfo_pkey PRIMARY KEY (rev);

ALTER TABLE ONLY role_permissions
ADD CONSTRAINT role_permissions_pkey PRIMARY KEY (role_rolename, permissions_permissionname);

ALTER TABLE ONLY "user"
ADD CONSTRAINT uk_ew1hvam8uwaknuaellwhqchhb UNIQUE (login);

ALTER TABLE ONLY project
ADD CONSTRAINT uk_rhwnvli52yuesut5a6tkjicaf UNIQUE (tag);

ALTER TABLE ONLY user_aud
ADD CONSTRAINT user_aud_pkey PRIMARY KEY (id, rev);

ALTER TABLE ONLY "user"
ADD CONSTRAINT user_pkey PRIMARY KEY (id);

ALTER TABLE ONLY user_project_role
ADD CONSTRAINT user_project_role_pkey PRIMARY KEY (id);

ALTER TABLE ONLY user_role
ADD CONSTRAINT user_role_pkey PRIMARY KEY (role_name);

ALTER TABLE ONLY comment
ADD CONSTRAINT fk_25f0go650rcnv6v8jav2xbp95 FOREIGN KEY (bug_tag) REFERENCES bug(tag);

ALTER TABLE ONLY user_project_role
ADD CONSTRAINT fk_36vpe3jfh7g8fjvnn56wc4s6c FOREIGN KEY (project_role_rolename) REFERENCES project_role(role_name);

ALTER TABLE ONLY bug
ADD CONSTRAINT fk_53vfy19sm49xe6yas2pays0dd FOREIGN KEY (reporter_id) REFERENCES "user"(id);

ALTER TABLE ONLY project_aud
ADD CONSTRAINT fk_6c8043eetuk2p340oo4yhjuqq FOREIGN KEY (rev) REFERENCES revinfo(rev);

ALTER TABLE ONLY user_project_role
ADD CONSTRAINT fk_6s5857565tdu1cmfsvs9etnnu FOREIGN KEY (project_id) REFERENCES project(id);

ALTER TABLE ONLY role_permissions
ADD CONSTRAINT fk_85gs2epmio128q6wdxbt188rx FOREIGN KEY (permissions_permissionname) REFERENCES permission(permission_name);

ALTER TABLE ONLY comment
ADD CONSTRAINT fk_9aq5p2jgf17y6b38x5ayd90oc FOREIGN KEY (author_id) REFERENCES "user"(id);

ALTER TABLE ONLY user_aud
ADD CONSTRAINT fk_f5gdy7vvtewl2ta5vhwjup6fd FOREIGN KEY (rev) REFERENCES revinfo(rev);

ALTER TABLE ONLY bug
ADD CONSTRAINT fk_gt52dbvtputxnu1rs5f42tr2w FOREIGN KEY (assignee_id) REFERENCES "user"(id);

ALTER TABLE ONLY bug_aud
ADD CONSTRAINT fk_h07gceldp3wgaxij9bgtmsxlf FOREIGN KEY (rev) REFERENCES revinfo(rev);

ALTER TABLE ONLY attachment
ADD CONSTRAINT fk_kmpdn1gfe0ii8hmp62h29mnyu FOREIGN KEY (bug_tag) REFERENCES bug(tag);

ALTER TABLE ONLY bug
ADD CONSTRAINT fk_lei7lg2ukqx0weaw62fik5045 FOREIGN KEY (project_id) REFERENCES project(id);

ALTER TABLE ONLY "user"
ADD CONSTRAINT fk_ow4gako4xhvsg0frb2c78tk3n FOREIGN KEY (user_role) REFERENCES user_role(role_name);

ALTER TABLE ONLY user_project_role
ADD CONSTRAINT fk_pxwh9fem0vnm25ad96i6f3xdf FOREIGN KEY (user_id) REFERENCES "user"(id);
