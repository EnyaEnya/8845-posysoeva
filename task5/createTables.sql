create table customer
(
    id         bigserial    not null
        constraint customer_id_pk
            primary key,
    first_name varchar(128) not null,
    last_name  varchar(128) not null,
    email      varchar(128) not null,
    phone      varchar(15)  not null
);

create table product
(
    id          bigserial    not null
        constraint product_id_pk
            primary key,
    title       varchar(128) not null,
    price       bigint       not null,
    description text         not null
);

create table order_table
(
    id          bigserial not null
        constraint order_id_pk
            primary key,
    customer_id bigint    not null
        constraint order_customer_id_fk
            references customer
);

create table order_entity
(
    id         bigserial not null
        constraint order_entity_id_pk
            primary key,
    order_id   bigint    not null
        constraint order_entity_order_id_fk
            references order_table,
    product_id bigint    not null
        constraint order_entity_product_id_fk
            references product,
    value      int       not null
);
