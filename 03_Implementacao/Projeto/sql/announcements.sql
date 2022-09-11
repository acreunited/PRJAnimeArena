use animearena;

create table ANNOUNCEMENT (
	announcementID int not null,
    title varchar(16) not null,
    category varchar(16) not null,
    smallDesc varChar(100) not null,
    fullDesc varchar(5000) not null,
    author  varchar(16) not null,
	postDate date not null,
    
	constraint pk_announcement primary key(announcementID)
);

ALTER TABLE ANNOUNCEMENT
ADD COLUMN author;

ALTER TABLE ANNOUNCEMENT
  ADD author int not null
    AFTER fullDesc;

ALTER TABLE ANNOUNCEMENT add
	constraint fk_author_admin foreign key (author) references ADMINISTRATOR(administratorID);
    
select * from ANNOUNCEMENT;

delete from ANNOUNCEMENT where announcementID=4;