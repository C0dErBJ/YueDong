﻿//------------------------------------------------------------------------------
// <auto-generated>
//     此代码已从模板生成。
//
//     手动更改此文件可能导致应用程序出现意外的行为。
//     如果重新生成代码，将覆盖对此文件的手动更改。
// </auto-generated>
//------------------------------------------------------------------------------

namespace Data.Model
{
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Infrastructure;
    
    public partial class YueDongEntities : DbContext
    {
        public YueDongEntities()
            : base("name=YueDongEntities")
        {
        }
    
        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            throw new UnintentionalCodeFirstException();
        }
    
        public virtual DbSet<CommonPicture> CommonPictures { get; set; }
        public virtual DbSet<CommonUser> CommonUsers { get; set; }
        public virtual DbSet<FreeTime> FreeTimes { get; set; }
        public virtual DbSet<Message> Messages { get; set; }
        public virtual DbSet<SportRecord> SportRecords { get; set; }
        public virtual DbSet<Stadium> Stadia { get; set; }
    }
}
