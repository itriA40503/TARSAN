--
-- ER/Studio Data Architect 8.5 SQL Code Generation
-- Company :      itri
-- Project :      TARSAN.dm1
-- Author :       c
--
-- Date Created : Thursday, September 03, 2015 11:01:53
-- Target DBMS : PostgreSQL 8.0
--

-- 
-- TABLE: ad 
--

CREATE TABLE ad(
    ad_id                BIGSERIAL,
    ad_group             int8,
    keywords             varchar(128),
    url                  varchar(2048),
    img                  varchar(2048),
    domain               varchar(128),
    shown_times          int4             DEFAULT 0 NOT NULL,
    clicked_times        int4             DEFAULT 0 NOT NULL,
    closed_times         int4             NOT NULL,
    weight               float4           NOT NULL,
    expire_date_time     timestamp,
    created_date_time    timestamp        NOT NULL,
    expired_flag         boolean          NOT NULL,
    deleted_flag         boolean          NOT NULL,
    CONSTRAINT "PK2" PRIMARY KEY (ad_id)
)
;



-- 
-- TABLE: ad_group 
--

CREATE TABLE ad_group(
    ad_group_id    BIGSERIAL,
    CONSTRAINT "PK11" PRIMARY KEY (ad_group_id)
)
;



-- 
-- TABLE: ad2user 
--

CREATE TABLE ad2user(
    ad2user_id           BIGSERIAL,
    ad_id                int8            NOT NULL,
    users_id             int8            NOT NULL,
    clicked_flag         boolean         NOT NULL,
    closed_flag          boolean         NOT NULL,
    identifier           varchar(128),
    created_date_time    timestamp       NOT NULL,
    CONSTRAINT "PK4" PRIMARY KEY (ad2user_id)
)
;



-- 
-- TABLE: pattern 
--

CREATE TABLE pattern(
    pattern_id           BIGSERIAL,
    url_host             varchar(128)     NOT NULL,
    url_path             varchar(2048),
    signature            varchar(128),
    pattern_type         int4             DEFAULT 1,
    web_type             int4             DEFAULT 99,
    created_date_time    timestamp        NOT NULL,
    last_access          timestamp,
    expired_flag         boolean          NOT NULL,
    deleted_flag         boolean          NOT NULL,
    confirmed_flag       boolean          NOT NULL,
    CONSTRAINT "PK6" PRIMARY KEY (pattern_id)
)
;



-- 
-- TABLE: pattern_type 
--

CREATE TABLE pattern_type(
    pattern_type_id    BIGSERIAL,
    name               varchar(64)     NOT NULL,
    remarks            varchar(512),
    CONSTRAINT "PK10" PRIMARY KEY (pattern_type_id)
)
;



-- 
-- TABLE: pattern2ad 
--

CREATE TABLE pattern2ad(
    pattern2ad_id    BIGSERIAL,
    pattern_id       int8    NOT NULL,
    ad_id            int8    NOT NULL,
    CONSTRAINT "PK5" PRIMARY KEY (pattern2ad_id)
)
;



-- 
-- TABLE: userevent 
--

CREATE TABLE userevent(
    userevent_id           BIGSERIAL,
    search_keyword         varchar(512),
    created_date_time      timestamp        NOT NULL,
    url_host               varchar(128),
    url_path               varchar(2048),
    session_id             varchar(128),
    ip                     varchar(64),
    product_search_flag    boolean,
    url_referer            varchar(2048),
    users_id               int8,
    pattern_id             int8,
    parent_id              int8,
    root_id                int8,
    CONSTRAINT "PK7" PRIMARY KEY (userevent_id)
)
;



-- 
-- TABLE: users 
--

CREATE TABLE users(
    users_id                 BIGSERIAL,
    account                  varchar(128)    NOT NULL,
    password                 varchar(128)    NOT NULL,
    deleted_flag             boolean         NOT NULL,
    tempkey                  varchar(128),
    last_active_date_time    timestamp,
    created_date_time        timestamp       NOT NULL,
    ip_address               varchar(128),
    CONSTRAINT "PK1" PRIMARY KEY (users_id)
)
;



-- 
-- TABLE: web_type 
--

CREATE TABLE web_type(
    web_type_id    BIGSERIAL,
    name           varchar(64)    NOT NULL,
    remarks        char(512),
    CONSTRAINT "PK9" PRIMARY KEY (web_type_id)
)
;



-- 
-- TABLE: ad2user 
--

ALTER TABLE ad2user ADD CONSTRAINT "Refad2" 
    FOREIGN KEY (ad_id)
    REFERENCES ad(ad_id)
;

ALTER TABLE ad2user ADD CONSTRAINT "Refusers3" 
    FOREIGN KEY (users_id)
    REFERENCES users(users_id)
;


-- 
-- TABLE: pattern2ad 
--

ALTER TABLE pattern2ad ADD CONSTRAINT "Refpattern4" 
    FOREIGN KEY (pattern_id)
    REFERENCES pattern(pattern_id)
;

ALTER TABLE pattern2ad ADD CONSTRAINT "Refad5" 
    FOREIGN KEY (ad_id)
    REFERENCES ad(ad_id)
;


-- 
-- TABLE: userevent 
--

ALTER TABLE userevent ADD CONSTRAINT "Refusers6" 
    FOREIGN KEY (users_id)
    REFERENCES users(users_id)
;

ALTER TABLE userevent ADD CONSTRAINT "Refpattern7" 
    FOREIGN KEY (pattern_id)
    REFERENCES pattern(pattern_id)
;


