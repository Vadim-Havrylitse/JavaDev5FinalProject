create table users(
    id uuid default random_uuid() primary key,
    user_name varchar(50),
    password varchar(50)
);

create table notes(
    id uuid default random_uuid() primary key,
    content varchar(10000),
    name varchar(50),
    access varchar(20),
    user_id uuid,
    foreign key (user_id) references users(id)
);