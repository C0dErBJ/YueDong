//------------------------------------------------------------------------------
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
    using System.Collections.Generic;
    
    public partial class CommonUser
    {
        public System.Guid Id { get; set; }
        public Nullable<System.DateTime> CreateTime { get; set; }
        public Nullable<System.DateTime> UpdateTime { get; set; }
        public string OperationUser { get; set; }
        public Nullable<int> Status { get; set; }
        public Nullable<int> Version { get; set; }
        public string UserName { get; set; }
        public string Password { get; set; }
        public string NickName { get; set; }
        public Nullable<System.DateTime> LoginTime { get; set; }
        public Nullable<int> Age { get; set; }
        public string Gender { get; set; }
        public string Hobby { get; set; }
        public string RecentLocation { get; set; }
    }
}