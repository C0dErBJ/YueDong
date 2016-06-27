using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Data.Model;

namespace Data.DAL
{
    public class CommonUserDAL
    {
        public bool Insert(CommonUser model)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    context.CommonUsers.Add(model);
                    
                    return context.SaveChanges() > 0;
                }
            }
            catch (Exception e)
            {

            }
            return false;
        }

        public CommonUser GetSingleById(Guid Id)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.CommonUsers.Where(a => a.Status > 0 && a.Id == Id);
                    if (result.Any())
                    {
                        return result.FirstOrDefault();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return null;
        }

        public List<CommonUser> GetList()
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.CommonUsers.Where(a => a.Status > 0);
                    if (result.Any())
                    {
                        return result.ToList();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return null;
        }

        public CommonUser GetSingleByNameAndPassword(string name, string password)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.CommonUsers.Where(a => a.Status > 0 && a.UserName == name && a.Password == password);
                    if (result.Any())
                    {
                        return result.FirstOrDefault();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return null;
        }

        public CommonUser GetSingleByName(string name)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.CommonUsers.Where(a => a.Status > 0 && a.UserName == name );
                    if (result.Any())
                    {
                        return result.FirstOrDefault();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return null;
        }

        public bool Update(CommonUser model)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.CommonUsers.FirstOrDefault(a => a.Id == model.Id);
                    if (result != null)
                    {
                        result.Password = model.Password;
                        result.Age = model.Age;
                        result.Gender = model.Gender;
                        result.Hobby = model.Hobby;
                        result.NickName = model.NickName;
                        result.UpdateTime = model.UpdateTime;
                    }
                    return context.SaveChanges() > 0;
                }
            }
            catch (Exception e)
            {

            }
            return false;
        }
    }
}
