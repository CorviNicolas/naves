create sequence nave_seq start with 1 increment by 50;
create table nave (nave_id bigint not null, nombre varchar(255) not null, primary key (nave_id));
create index idx_nave_nombre on nave (nombre);
