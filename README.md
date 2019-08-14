# dbArticlesCrawler
crawler db articles from internet base on crawler4j


# 实现步骤
通过site.xml文件中配置的网址以及元素标签位置爬取CSDN、ITPUB等等互联网上数据库相关的文章，然后批量入库

在resource下的有示例，通过列表页面获取文章的标题、链接、摘要

# create table
```sql
CREATE TABLE public.dh_article_stream
(
    dh_id bigint NOT NULL DEFAULT nextval('rh_article_stream_rh_id_seq'::regclass),
    title character varying(256),
    preview_content text COLLATE ,
    link character varying(512),
    crawl_time timestamp(0) without time zone,
    view_count integer DEFAULT 0,
    type smallint,
    origin character varying(64),
    featured boolean DEFAULT false,
    like_count integer DEFAULT 0,
    base_url character varying(128),
    CONSTRAINT dh_article_stream_pkey PRIMARY KEY (dh_id)
);

CREATE UNIQUE INDEX dh_article_stream_link_uindex ﻿ON public.dh_article_stream (link);

CREATE SEQUENCE rh_article_stream_rh_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

```

# run
```java
java -jar **.jar /u01/dbhub/site.xml
```
