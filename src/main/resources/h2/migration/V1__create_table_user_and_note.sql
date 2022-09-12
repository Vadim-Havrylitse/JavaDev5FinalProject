create table users(
    id VARCHAR(255) NOT NULL DEFAULT(uuid()) primary key,
    user_name varchar(50),
    password varchar(255)
);

create table notes(
    id VARCHAR(255) NOT NULL DEFAULT(uuid()) primary key,
    content varchar(10000),
    name varchar(50),
    access varchar(20),
    user_id VARCHAR(255),
    foreign key (user_id) references users(id)
);