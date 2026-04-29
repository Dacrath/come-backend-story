create table user (
    id BIGINT primary key,
    first_name varchar(80),
    last_name varchar(80),
    group_id BIGINT,
    email varchar(100),
    cell_number varchar(15),
);

create table job_code (
    id BIGINT primary key,
    name varchar(80)
);

create table groups (
    id BIGINT primary key,
    name varchar(80)
);


create table timesheets (
    id BIGINT primary key
    user_id BIGINT,
    jobcode_id BIGINT,
    start timestamptz,
    end timestamptz,
    duration INT,
    on_the_clock bool,
    notes varchar(4000),
    last_modified
);



