CREATE TABLE "categories" (
	"cat_id" serial NOT NULL,
	"cat_name" varchar(255) NOT NULL UNIQUE,
	"cat_description" varchar(255) NOT NULL,
	CONSTRAINT categories_pk PRIMARY KEY ("cat_id")
) WITH (
  OIDS=FALSE
);

CREATE TABLE "users" (
	"user_id" serial NOT NULL,
	"user_name" varchar(30) NOT NULL UNIQUE,
	"user_pw" varchar(255) NOT NULL,
	"is_user" boolean NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY ("user_id")
) WITH (
  OIDS=FALSE
);

CREATE TABLE "conversations" (
	"convo_id" serial NOT NULL,
	"convo_cat" integer NOT NULL,
	"convo_by" integer NOT NULL,
	"convo_time" timestamp with time zone NOT NULL,
	CONSTRAINT conversations_pk PRIMARY KEY ("convo_id")
) WITH (
  OIDS=FALSE	
);

CREATE TABLE "replies" (
	"rep_id" serial NOT NULL,
	"rep_message" text NOT NULL,
	"ref_convo_id" integer NOT NULL,
	"rep_by" integer NOT NULL,
	"rep_time" timestamp with time zone NOT NULL,
	CONSTRAINT replies_pk PRIMARY KEY ("rep_id")
) WITH (
  OIDS=FALSE
);

ALTER TABLE "conversations" ADD CONSTRAINT "conversations_fk0" FOREIGN KEY ("convo_cat") REFERENCES "categories"("cat_id");
ALTER TABLE "conversations" ADD CONSTRAINT "conversations_fk1" FOREIGN KEY ("convo_by") REFERENCES "users"("user_id");

ALTER TABLE "replies" ADD CONSTRAINT "replies_fk0" FOREIGN KEY ("related_to_convo") REFERENCES "conversations"("convo_id");
ALTER TABLE "replies" ADD CONSTRAINT "replies_fk1" FOREIGN KEY ("rep_by") REFERENCES "users"("user_id");

INSERT INTO categories VALUES (1,'education','where you can discuss educational problem');

-- INSERT INTO users VALUES (1,'huy','e19d5cd5af0378da05f63f891c7467af','\\001'); 
-- INSERT INTO users VALUES (2,'tuan','865541678b41810342b917f8c79d5137','\\000');
-- INSERT INTO users VALUES (3,'binh','a6a9ab0e57c78ca08ba5f5cd0b0f3ecd','\\001');
INSERT INTO users (user_name,user_pw,is_user) VALUES ('huy','abcd1234','1'),('tuan','boylvu','0'),('binh','skynet1128','1');

-- INSERT INTO conversations (convo_id,convo_cat,convo_by,convo_time) VALUES (1,1,1,CURRENT_TIMESTAMP),(2,1,3,CURRENT_TIMESTAMP);
INSERT INTO conversations (convo_cat,convo_by,convo_time) VALUES (1,1,CURRENT_TIMESTAMP),(1,3,CURRENT_TIMESTAMP);

-- INSERT INTO replies (rep_id,rep_message,related_to_convo,rep_by,rep_time) VALUES
-- (1,'hello',1,1,CURRENT_TIMESTAMP),(2,'hi',2,3,CURRENT_TIMESTAMP)
-- ,(3,'hello, huy',1,2,CURRENT_TIMESTAMP),(4,'hello,binh',2,2,CURRENT_TIMESTAMP);
INSERT INTO replies (rep_message,ref_convo_id,rep_by,rep_time) VALUES
('hello',1,1,CURRENT_TIMESTAMP),('hi',2,3,CURRENT_TIMESTAMP)
,('hello, huy',1,2,CURRENT_TIMESTAMP),('hello,binh',2,2,CURRENT_TIMESTAMP);


-- SELECT tat ca messages cua 1 nguoi, noi voi ai, trong convo nao
/*SELECT users.user_name,replies.rep_message,replies.ref_convo_id FROM users
INNER JOIN replies ON users.user_id = replies.rep_by
ORDER BY rep_id ASC;*/

-- cho biet id cua 1 user
/*SELECT user_id FROM users
WHERE user_name = 'tuan'; -- neu user ko co thi select ko cho ra ket qua gi*/

--SELECT message moi nhat cua moi convo, va message do cua ai gui
/*SELECT ref_convo_id,rep_message,users.user_name FROM replies
INNER JOIN users ON users.user_id = replies.rep_by
WHERE rep_id IN(SELECT MAX(replies.rep_id) FROM replies
				INNER JOIN conversations
				ON conversations.convo_id = replies.ref_convo_id
				GROUP BY replies.ref_convo_id);*/

/*
SELECT users.user_name,replies.rep_message FROM replies
INNER JOIN users ON replies.rep_by = users.user_id 
WHERE ref_convo_id = 2 ORDER BY rep_id ASC;

SELECT users.user_name,replies.rep_message,replies.ref_convo_id FROM users
INNER JOIN replies ON users.user_id = replies.rep_by
ORDER BY rep_id ASC;*/
