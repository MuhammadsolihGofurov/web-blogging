insert into profile(name, username, password, status, visible, created_at)
values ("adminBek", "222211m@jdu.uz", "secret", "ACTIVE", true, now());

-- SELECT setval('profile_id_seq', max(id)) FROM profile;

insert into profile_role(profile_id, roles, created_at)
values ((SELECT MAX(id) FROM profile), "ROLE_ADMIN", now()),
       ((SELECT MAX(id) FROM profile), "ROLE_USER", now());