# TTsukutter schema

# --- !Ups

/* Create Tables */

-- Twitterから取得したアプリネタを格納するトランザクションテーブル。
CREATE TABLE T_APPLICATION_IDEA
(
	-- アプリネタIDで、トランザクションを一意に判別するIDで、Twitterから取得したTweetIDをそのまま利用する。
	IDEA_ID BIGINT NOT NULL UNIQUE,
	-- ネタを投下したTwitterユーザのIDで、Tweetの投稿者から取得する。
	IDEAMAN_ID BIGINT NOT NULL,
	-- ネタツイートのから取得した概要で、ツイートから@リプライとハッシュタグをのぞいたものとなる。
	IDEA_DESCRIPTION VARCHAR(140),
	PRIMARY KEY (IDEA_ID)
);


-- アプリネタに対して登録された公開アプリケーションを格納するトランザクション。
CREATE TABLE T_APPLICATION_ISSUE
(
	-- アプリネタIDで、トランザクションを一意に判別するIDで、Twitterから取得したTweetIDをそのまま利用する。
	IDEA_ID BIGINT NOT NULL,
	-- 公開されたアプリケーションのIDで、連番で設定する。
	SEQ_NO INT NOT NULL,
	-- アプリケーションの概要で、ユーザが登録する。
	ISSUE_DESCLIPTION VARCHAR(140),
	-- 公開アプリのURLを格納する。
	ISSUE_URL VARCHAR(1024),
	-- 作り手のシステム内ID
	CRAFTMAN_ID INT,
	PRIMARY KEY (IDEA_ID, SEQ_NO)
);

CREATE TABLE M_TT_USER
(
	-- TTsukutterユーザのID
	USER_ID SERIAL NOT NULL UNIQUE,
	-- システム上に表示するための名前。Twitterの表示名の仕様と合わせて15文字を上限とする。
	USER_NAME VARCHAR(15),
	-- 表示用のユーザプロフィール。Twitterの仕様と合わせて160文字としている。
	USER_PROFILE VARCHAR(160),
	-- 認証方法を示すコード。
	-- 1:OAuth認証(Twitter)
	-- 2:通常認証(メールアドレス+パスワード)
	AUTH_TYPE INT,
	-- 認証に使用する一つ目の情報。
	-- OAuth認証の場合はアクセストークン、通常認証の場合はメールアドレスとなる。
	AUTH_INFO_1 VARCHAR(100),
	-- 認証に使用する一つ目の情報。
	-- OAuth認証の場合はトークンシークレット、通常認証の場合はパスワードとなる。
	AUTH_INFO_2 VARCHAR(100),
	PRIMARY KEY (USER_ID)
);


-- Twitterから取得したアプリネタを格納するトランザクションテーブル。
CREATE TABLE T_APPLICATION_IDEA
(
	-- アプリネタIDで、トランザクションを一意に判別するIDで、Twitterから取得したTweetIDをそのまま利用する。
	IDEA_ID BIGINT NOT NULL UNIQUE,
	-- ネタを投下したTwitterユーザのIDで、Tweetの投稿者から取得する。
	IDEAMAN_ID BIGINT NOT NULL,
	-- ネタツイートのから取得した概要で、ツイートから@リプライとハッシュタグをのぞいたものとなる。
	IDEA_DESCRIPTION VARCHAR(140),
	PRIMARY KEY (IDEA_ID)
);

CREATE TABLE T_USER_SITE
(
	-- TTsukutterユーザのID
	USER_ID INT NOT NULL UNIQUE,
	-- ユーザごとのサイト情報を一意に識別する
	SITE_ID INT NOT NULL,
	-- ユーザサイトの表示順を示す
	DESPLAY_NO INT,
	-- ユーザサイトのURL
	SITE_URL VARCHAR(1024),
	PRIMARY KEY (USER_ID, SITE_ID)
);



/* Create Foreign Keys */

ALTER TABLE T_APPLICATION_ISSUE
	ADD FOREIGN KEY (CRAFTMAN_ID)
	REFERENCES M_TT_USER (USER_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE T_USER_FAVORITE
	ADD FOREIGN KEY (USER_ID)
	REFERENCES M_TT_USER (USER_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE T_USER_SITE
	ADD FOREIGN KEY (USER_ID)
	REFERENCES M_TT_USER (USER_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE T_APPLICATION_ISSUE
	ADD FOREIGN KEY (IDEA_ID)
	REFERENCES T_APPLICATION_IDEA (IDEA_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE T_USER_FAVORITE
	ADD FOREIGN KEY (IDEA_ID)
	REFERENCES T_APPLICATION_IDEA (IDEA_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;

/* Create Indexes */

-- ネタ主を元に検索するためのインデックス
CREATE INDEX IDX_SEARCH_FOR_IDEAMAN ON T_APPLICATION_IDEA USING BTREE (IDEAMAN_ID);
-- 作り手から検索するケースを想定したインデックス
CREATE INDEX IDX_SAERCH_FOR_CRAFTMAN ON T_APPLICATION_ISSUE USING BTREE (CRAFTMAN_ID);

# --- !Downs

/* Drop Indexes */

DROP INDEX IF EXISTS IDX_SEARCH_FOR_IDEAMAN;
DROP INDEX IF EXISTS IDX_SAERCH_FOR_CRAFTMAN;



/* Drop Tables */

DROP TABLE IF EXISTS T_APPLICATION_ISSUE;
DROP TABLE IF EXISTS T_USER_FAVORITE;
DROP TABLE IF EXISTS T_USER_SITE;
DROP TABLE IF EXISTS M_TT_USER;
DROP TABLE IF EXISTS T_APPLICATION_IDEA;