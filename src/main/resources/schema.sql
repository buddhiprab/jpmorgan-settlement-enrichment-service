create schema if not exists api;

create table if not exists api.ssi_data
(
    id               int auto_increment primary key,
    ssi_code         varchar2(20) not null,
    payer_acc_num    varchar2(20) not null,
    payer_bank       varchar2(50) not null,
    receiver_acc_num varchar2(20) not null,
    receiver_bank    varchar2(50) not null,
    info             varchar2(50) not null,
    creation_time    timestamp default systimestamp
);

create table if not exists api.trade_request
(
    id            number auto_increment primary key,
    trade_id      varchar2(20)  not null,
    ssi_code      varchar2(20)  not null,
    amount        number(18, 2) not null,
    currency      varchar2(3)   not null,
    value_date    varchar2(50)  not null,
    creation_time timestamp default systimestamp
);

create table if not exists api.settlement_message
(
    id            number auto_increment primary key,
    message_id    varchar2(50) not null,
    message       text         not null,
    creation_time timestamp default systimestamp
);

insert into api.ssi_data (ssi_code, payer_acc_num, payer_bank, receiver_acc_num, receiver_bank, info, creation_time)
values ('DBS_OCBC_1', '05461368', 'DBSSGB2LXXX', '438421', 'OCBCSGSGXXX', 'BNF:PAY CLIENT', current_timestamp);
insert into api.ssi_data (ssi_code, payer_acc_num, payer_bank, receiver_acc_num, receiver_bank, info, creation_time)
values ('OCBC_DBS_1', '438421', 'OCBCSGSGXXX', '05461368', 'DBSSGB2LXXX', 'BNF:FFC-4697132', current_timestamp);
insert into api.ssi_data (ssi_code, payer_acc_num, payer_bank, receiver_acc_num, receiver_bank, info, creation_time)
values ('OCBC_DBS_2', '438421', 'OCBCSGSGXXX', '05461369', 'DBSSSGSGXXX', 'BNF:FFC-482315', current_timestamp);
insert into api.ssi_data (ssi_code, payer_acc_num, payer_bank, receiver_acc_num, receiver_bank, info, creation_time)
values ('DBS_SCB', '185586', 'DBSSSGSGXXX', '1868422', 'SCBLAU2SXXX', 'RFB:Test payment', current_timestamp);
insert into api.ssi_data (ssi_code, payer_acc_num, payer_bank, receiver_acc_num, receiver_bank, info, creation_time)
values ('CITI_GS', '00454983', 'CITIGB2LXXX', '48486414', 'GSCMUS33XXX', '', current_timestamp);