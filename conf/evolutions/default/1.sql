# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table basket (
  id                            bigint not null,
  customer_email                varchar(255),
  constraint uq_basket_customer_email unique (customer_email),
  constraint pk_basket primary key (id)
);
create sequence basket_seq;

create table category (
  id                            bigint not null,
  name                          varchar(255),
  constraint pk_category primary key (id)
);
create sequence category_seq;

create table category_product (
  category_id                   bigint not null,
  product_id                    bigint not null,
  constraint pk_category_product primary key (category_id,product_id)
);

create table contact_us (
  id                            bigint not null,
  administrator_email           varchar(255),
  customer_email                varchar(255),
  message                       Text,
  message_date                  timestamp,
  constraint pk_contact_us primary key (id)
);
create sequence contact_us_seq;

create table credit_card (
  id                            bigint not null,
  customer_email                varchar(255),
  type                          varchar(255),
  num                           varchar(255),
  exp_date                      varchar(255),
  constraint uq_credit_card_customer_email unique (customer_email),
  constraint pk_credit_card primary key (id)
);
create sequence credit_card_seq;

create table order_item (
  id                            bigint not null,
  order_id                      bigint,
  basket_id                     bigint,
  product_id                    bigint,
  quantity                      integer,
  price                         double,
  constraint pk_order_item primary key (id)
);
create sequence order_item_seq;

create table product (
  id                            bigint not null,
  name                          varchar(255),
  description                   Text,
  stock                         integer,
  price                         double,
  trailer                       varchar(255),
  copies_sold                   integer,
  constraint pk_product primary key (id)
);
create sequence product_seq;

create table review (
  id                            bigint not null,
  product_id                    bigint,
  customer_email                varchar(255),
  review                        Text,
  review_date                   timestamp,
  constraint pk_review primary key (id)
);
create sequence review_seq;

create table shop_order (
  id                            bigint not null,
  order_date                    timestamp,
  customer_email                varchar(255),
  order_total                   double,
  constraint pk_shop_order primary key (id)
);
create sequence shop_order_seq;

create table user (
  role                          varchar(255),
  email                         varchar(255) not null,
  name                          varchar(255),
  password                      varchar(255),
  street1                       varchar(255),
  street2                       varchar(255),
  town                          varchar(255),
  post_code                     varchar(255),
  points                        integer,
  constraint pk_user primary key (email)
);

alter table basket add constraint fk_basket_customer_email foreign key (customer_email) references user (email) on delete restrict on update restrict;

alter table category_product add constraint fk_category_product_category foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_category_product_category on category_product (category_id);

alter table category_product add constraint fk_category_product_product foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_category_product_product on category_product (product_id);

alter table contact_us add constraint fk_contact_us_administrator_email foreign key (administrator_email) references user (email) on delete restrict on update restrict;
create index ix_contact_us_administrator_email on contact_us (administrator_email);

alter table contact_us add constraint fk_contact_us_customer_email foreign key (customer_email) references user (email) on delete restrict on update restrict;
create index ix_contact_us_customer_email on contact_us (customer_email);

alter table credit_card add constraint fk_credit_card_customer_email foreign key (customer_email) references user (email) on delete restrict on update restrict;

alter table order_item add constraint fk_order_item_order_id foreign key (order_id) references shop_order (id) on delete restrict on update restrict;
create index ix_order_item_order_id on order_item (order_id);

alter table order_item add constraint fk_order_item_basket_id foreign key (basket_id) references basket (id) on delete restrict on update restrict;
create index ix_order_item_basket_id on order_item (basket_id);

alter table order_item add constraint fk_order_item_product_id foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_order_item_product_id on order_item (product_id);

alter table review add constraint fk_review_product_id foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_review_product_id on review (product_id);

alter table review add constraint fk_review_customer_email foreign key (customer_email) references user (email) on delete restrict on update restrict;
create index ix_review_customer_email on review (customer_email);

alter table shop_order add constraint fk_shop_order_customer_email foreign key (customer_email) references user (email) on delete restrict on update restrict;
create index ix_shop_order_customer_email on shop_order (customer_email);


# --- !Downs

alter table basket drop constraint if exists fk_basket_customer_email;

alter table category_product drop constraint if exists fk_category_product_category;
drop index if exists ix_category_product_category;

alter table category_product drop constraint if exists fk_category_product_product;
drop index if exists ix_category_product_product;

alter table contact_us drop constraint if exists fk_contact_us_administrator_email;
drop index if exists ix_contact_us_administrator_email;

alter table contact_us drop constraint if exists fk_contact_us_customer_email;
drop index if exists ix_contact_us_customer_email;

alter table credit_card drop constraint if exists fk_credit_card_customer_email;

alter table order_item drop constraint if exists fk_order_item_order_id;
drop index if exists ix_order_item_order_id;

alter table order_item drop constraint if exists fk_order_item_basket_id;
drop index if exists ix_order_item_basket_id;

alter table order_item drop constraint if exists fk_order_item_product_id;
drop index if exists ix_order_item_product_id;

alter table review drop constraint if exists fk_review_product_id;
drop index if exists ix_review_product_id;

alter table review drop constraint if exists fk_review_customer_email;
drop index if exists ix_review_customer_email;

alter table shop_order drop constraint if exists fk_shop_order_customer_email;
drop index if exists ix_shop_order_customer_email;

drop table if exists basket;
drop sequence if exists basket_seq;

drop table if exists category;
drop sequence if exists category_seq;

drop table if exists category_product;

drop table if exists contact_us;
drop sequence if exists contact_us_seq;

drop table if exists credit_card;
drop sequence if exists credit_card_seq;

drop table if exists order_item;
drop sequence if exists order_item_seq;

drop table if exists product;
drop sequence if exists product_seq;

drop table if exists review;
drop sequence if exists review_seq;

drop table if exists shop_order;
drop sequence if exists shop_order_seq;

drop table if exists user;

