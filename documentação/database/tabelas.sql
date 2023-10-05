CREATE TABLE  "TB_PF"
(    "ID_PESSOA" NUMBER(19,0),
     "NM_PESSOA" VARCHAR2(255 BYTE),
     "DT_NASCIMENTO" DATE,
     "TP_PESSOA" VARCHAR2(50 BYTE),
     "NR_CPF" VARCHAR2(50 BYTE)
) ;

CREATE TABLE "TB_PJ"
(    "ID_PESSOA" NUMBER(19,0),
     "NM_PESSOA" VARCHAR2(255 BYTE),
     "DT_NASCIMENTO" DATE,
     "TP_PESSOA" VARCHAR2(50 BYTE),
     "NR_CNPJ" VARCHAR2(50 BYTE)
) ;

create sequence SQ_PESSOAS;

create or replace trigger TG_SQ_PF
    before insert or update on TB_PF
                                for each row begin
    if inserting and :new.ID_PESSOA is null then
        :new.ID_PESSOA := SQ_PESSOAS.nextval;
end if;
end;

create or replace trigger TG_SQ_PJ
    before insert or update on TB_PJ
                                for each row begin
    if inserting and :new.ID_PESSOA is null then
        :new.ID_PESSOA := SQ_PESSOAS.nextval;
end if;
end;