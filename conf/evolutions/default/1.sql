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
	IDEA_ID BIGINT NOT NULL UNIQUE,
	-- 公開されたアプリケーションのIDで、連番で設定する。
	SEQ_NO BIGINT NOT NULL UNIQUE,
	-- アプリケーションの概要で、ユーザが登録する。
	ISSUE_DESCLIPTION VARCHAR(140),
	-- 公開アプリのURLを格納する。
	ISSUE_URL VARCHAR(1024),
	-- 作り手のシステム内ID
	CRAFTMAN_ID INT,
	PRIMARY KEY (IDEA_ID, SEQ_NO)
);



/* Create Foreign Keys */

ALTER TABLE T_APPLICATION_ISSUE
	ADD FOREIGN KEY (IDEA_ID)
	REFERENCES T_APPLICATION_IDEA (IDEA_ID)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;

# --- !Downs

DROP TABLE IF EXISTS T_APPLICATION_ISSUE;
DROP TABLE IF EXISTS T_APPLICATION_IDEA;