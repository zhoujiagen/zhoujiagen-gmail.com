CREATE TABLE `user`.`users` (
    `id`              BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`   		VARCHAR(255)          DEFAULT NULL COMMENT '姓名',
    `created_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`, `org_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 21
    DEFAULT CHARSET = UTF8MB4 COMMENT ='用户';
