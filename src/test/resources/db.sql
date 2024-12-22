CREATE SEQUENCE hibernate_sequence START 100;
create table friendship
(
        uuid uuid primary key,
        account_id_to uuid,
        account_id_from uuid,
        status_between  varchar(255)
);

insert into friendship(
            	uuid, account_id_to, account_id_from, status_between)
values ('55fa7c73-0964-495a-92ab-98463c56dd02', '1cfa7c73-0964-495a-92ab-98463c56dd03', '1cfa7c73-0964-495a-92ab-98463c56dd04', 'BLOCKED'),
       ('55fa7c73-0964-495a-92ab-98463c56dd03', '1cfa7c73-0964-495a-92ab-98463c56dd03', '1cfa7c73-0964-495a-92ab-98463c56dd04', 'REQUEST_FROM'),
       ('55fa7c73-0964-495a-92ab-98463c56dd04', '1cfa7c73-0964-495a-92ab-98463c56dd03', '1cfa7c73-0964-495a-92ab-98463c56dd04', 'SUBSCRIBED'),
       ('55fa7c73-0964-495a-92ab-98463c56dd05', '1cfa7c73-0964-495a-92ab-98463c56dd03', '1cfa7c73-0964-495a-92ab-98463c56dd04', 'FRIEND');



