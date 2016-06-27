
-- --------------------------------------------------
-- Entity Designer DDL Script for SQL Server 2005, 2008, 2012 and Azure
-- --------------------------------------------------
-- Date Created: 02/21/2016 00:38:18
-- Generated from EDMX file: C:\YueDongProject\YueDong\Data\Model\Model.edmx
-- --------------------------------------------------

SET QUOTED_IDENTIFIER OFF;
GO
USE [YueDong];
GO
IF SCHEMA_ID(N'dbo') IS NULL EXECUTE(N'CREATE SCHEMA [dbo]');
GO

-- --------------------------------------------------
-- Dropping existing FOREIGN KEY constraints
-- --------------------------------------------------


-- --------------------------------------------------
-- Dropping existing tables
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[CommonPictures]', 'U') IS NOT NULL
    DROP TABLE [dbo].[CommonPictures];
GO
IF OBJECT_ID(N'[dbo].[CommonUsers]', 'U') IS NOT NULL
    DROP TABLE [dbo].[CommonUsers];
GO
IF OBJECT_ID(N'[dbo].[FreeTimes]', 'U') IS NOT NULL
    DROP TABLE [dbo].[FreeTimes];
GO
IF OBJECT_ID(N'[dbo].[Messages]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Messages];
GO
IF OBJECT_ID(N'[dbo].[SportRecords]', 'U') IS NOT NULL
    DROP TABLE [dbo].[SportRecords];
GO
IF OBJECT_ID(N'[dbo].[Stadia]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Stadia];
GO

-- --------------------------------------------------
-- Creating all tables
-- --------------------------------------------------

-- Creating table 'CommonPictures'
CREATE TABLE [dbo].[CommonPictures] (
    [Id] uniqueidentifier  NOT NULL,
    [CreateTime] datetime  NULL,
    [Status] int  NULL,
    [FileData] varbinary(max)  NULL,
    [UserId] uniqueidentifier  NULL
);
GO

-- Creating table 'CommonUsers'
CREATE TABLE [dbo].[CommonUsers] (
    [Id] uniqueidentifier  NOT NULL,
    [CreateTime] datetime  NULL,
    [UpdateTime] datetime  NULL,
    [OperationUser] varchar(100)  NULL,
    [Status] int  NULL,
    [Version] int  NULL,
    [UserName] varchar(50)  NULL,
    [Password] varchar(100)  NULL,
    [NickName] varchar(150)  NULL,
    [LoginTime] datetime  NULL,
    [Age] int  NULL,
    [Gender] varchar(50)  NULL,
    [Hobby] varchar(500)  NULL,
    [RecentLocation] varchar(300)  NULL
);
GO

-- Creating table 'FreeTimes'
CREATE TABLE [dbo].[FreeTimes] (
    [Id] uniqueidentifier  NOT NULL,
    [CreateTime] datetime  NULL,
    [UpdateTime] datetime  NULL,
    [OperationUser] varchar(100)  NULL,
    [Status] int  NULL,
    [Version] int  NULL,
    [UserId] uniqueidentifier  NULL,
    [StartTime] datetime  NULL,
    [EndTime] datetime  NULL,
    [Week] int  NULL
);
GO

-- Creating table 'Messages'
CREATE TABLE [dbo].[Messages] (
    [Id] uniqueidentifier  NOT NULL,
    [CreateTime] datetime  NULL,
    [Status] int  NULL,
    [FromWho] uniqueidentifier  NULL,
    [ToWho] uniqueidentifier  NULL,
    [Text] varchar(max)  NULL,
    [PicId] uniqueidentifier  NULL,
    [MessageBatch] varchar(150)  NULL
);
GO

-- Creating table 'SportRecords'
CREATE TABLE [dbo].[SportRecords] (
    [Id] uniqueidentifier  NOT NULL,
    [CreateTime] datetime  NULL,
    [UpdateTime] datetime  NULL,
    [OperationUser] varchar(100)  NULL,
    [Status] int  NULL,
    [Version] int  NULL,
    [SportLastTime] int  NULL,
    [UserId] uniqueidentifier  NULL
);
GO

-- Creating table 'Stadia'
CREATE TABLE [dbo].[Stadia] (
    [Id] uniqueidentifier  NOT NULL,
    [CreateTime] datetime  NULL,
    [UpdateTime] datetime  NULL,
    [OperationUser] varchar(100)  NULL,
    [Status] int  NULL,
    [Version] int  NULL,
    [Name] varchar(150)  NULL,
    [Address] varchar(100)  NULL,
    [OpenStartTime] datetime  NULL,
    [OpenEndTime] datetime  NULL,
    [Price] varchar(250)  NULL,
    [Phone] varchar(250)  NOT NULL,
    [Longitude] float  NOT NULL,
    [Latitude] float  NOT NULL
);
GO

-- --------------------------------------------------
-- Creating all PRIMARY KEY constraints
-- --------------------------------------------------

-- Creating primary key on [Id] in table 'CommonPictures'
ALTER TABLE [dbo].[CommonPictures]
ADD CONSTRAINT [PK_CommonPictures]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'CommonUsers'
ALTER TABLE [dbo].[CommonUsers]
ADD CONSTRAINT [PK_CommonUsers]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'FreeTimes'
ALTER TABLE [dbo].[FreeTimes]
ADD CONSTRAINT [PK_FreeTimes]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'Messages'
ALTER TABLE [dbo].[Messages]
ADD CONSTRAINT [PK_Messages]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'SportRecords'
ALTER TABLE [dbo].[SportRecords]
ADD CONSTRAINT [PK_SportRecords]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'Stadia'
ALTER TABLE [dbo].[Stadia]
ADD CONSTRAINT [PK_Stadia]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- --------------------------------------------------
-- Creating all FOREIGN KEY constraints
-- --------------------------------------------------

-- --------------------------------------------------
-- Script has ended
-- --------------------------------------------------