alter table NEWSAPP_NEWS add constraint FK_NEWSAPP_NEWS_ON_CATEGORY foreign key (CATEGORY_ID) references NEWSAPP_NEWS_CATEGORY(ID);
create index IDX_NEWSAPP_NEWS_ON_CATEGORY on NEWSAPP_NEWS (CATEGORY_ID);
