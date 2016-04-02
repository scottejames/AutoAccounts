
    drop table if exists Catagory;

    drop table if exists CatagoryRegexp;

    drop table if exists Cheque;

    drop table if exists PayeeRegExp;

    drop table if exists Transaction;

    create table Catagory (
        ID bigint not null auto_increment,
        Direction integer,
        Name varchar(255),
        primary key (ID)
    );

    create table CatagoryRegexp (
        ID bigint not null auto_increment,
        _catagoryId bigint,
        notes varchar(255),
        regex varchar(255),
        primary key (ID)
    );

    create table Cheque (
        ID bigint not null auto_increment,
        Amount numeric(19,2),
        CashedDate date,
        Comment varchar(255),
        ChequeNumber varchar(255),
        Payee varchar(255),
        RawData varchar(255),
        Void bit,
        WrittenDate date,
        primary key (ID)
    );

    create table PayeeRegExp (
        ID bigint not null auto_increment,
        FromRegex varchar(255),
        ToRegexp varchar(255),
        primary key (ID)
    );

    create table Transaction (
        ID bigint not null auto_increment,
        Amount numeric(19,2),
        Catagory bigint,
        ChequeNotMatched bit,
        ChequeNumber varchar(255),
        ChequeWrittenDate date,
        Date date,
        EnrichedPayee varchar(255),
        FileName varchar(255),
        IsCheque bit,
        ManualEditCatagory bit,
        ManualEditPayee bit,
        Payee varchar(255),
        Version integer,
        primary key (ID)
    );
