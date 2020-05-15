create schema if not exists ses;

create table if not exists ses.ssi_data
(
    id                  int            auto_increment primary key,
    ssi_code            varchar2(20)   not null,
    payer_acc_num       varchar2(20)   not null,
    payer_bank          varchar2(50)   not null,
    receiver_acc_num    varchar2(20)   not null,
    receiver_bank       varchar2(50)   not null,
    info                varchar2(50)   not null,
    creation_time       timestamp      default systimestamp
);

create table if not exists ses.trade_request
(
    id                  number          auto_increment primary key,
    trade_id            varchar2(20)    not null,
    ssi_code            varchar2(20)    not null,
    amount              number(18,2)    not null,
    currency            varchar2(3)     not null,
    value_date          varchar2(50)    not null,
    creation_time       timestamp       default systimestamp
);

create table if not exists ses.settlement_message
(
    id                  number          auto_increment primary key,
    message_id          varchar2(50)    not null,
    message             text            not null,
    creation_time       timestamp       default systimestamp
);