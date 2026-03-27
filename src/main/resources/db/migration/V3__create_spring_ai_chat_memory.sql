-- COPIADO DO QUE O SPRING GERA QUANDO ESTÁ DEFINIDO PARA ELE CRIAR.
CREATE TABLE public.spring_ai_chat_memory
(
    conversation_id varchar(36) NOT NULL,
    "content"       text        NOT NULL,
    "type"          varchar(10) NOT NULL,
    "timestamp"     timestamp   NOT NULL,
    CONSTRAINT spring_ai_chat_memory_type_check CHECK (((type)::text = ANY ((ARRAY['USER':: character varying, 'ASSISTANT':: character varying, 'SYSTEM':: character varying, 'TOOL':: character varying])::text[])
) )
);
CREATE INDEX spring_ai_chat_memory_conversation_id_timestamp_idx ON public.spring_ai_chat_memory USING btree (conversation_id, "timestamp");