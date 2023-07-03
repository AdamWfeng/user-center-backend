create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    userName     varchar(256)                       null comment '用户名称',
    userAccount  varchar(256)                       null comment '账号',
    avatarUrl    varchar(256)                       null comment '用户头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(256)                       not null comment '密码',
    phone        varchar(128)                       null comment '电话号码',
    email        varchar(512)                       null comment '邮箱',
    userRole     int      default 0                 null,
    userStatus   int      default 0                 null comment '用户状态',
    createTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    isDelete     tinyint  default 0                 null comment '逻辑删除'
);

