using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Data.Model;

namespace Data.DAL
{
    public class MessageDAL
    {
        public bool Insert(Message model)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    context.Messages.Add(model);

                    return context.SaveChanges() > 0;
                }
            }
            catch (Exception e)
            {

            }
            return false;
        }

        public Message GetSingleById(Guid Id)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.Messages.Where(a => a.Status > 0 && a.Id == Id);
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

        public List<Message> GetList()
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.Messages.Where(a => a.Status > 0);
                    if (result.Any())
                    {
                        return result.ToList();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return new List<Message>();
        }

        public List<Message> GetListByBatch(string batch)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.Messages.Where(a => a.Status > 0 && a.MessageBatch == batch);
                    if (result.Any())
                    {
                        return result.ToList();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return new List<Message>();
        }

        public List<Message> GetListByUserId(Guid UserId)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.Messages.Where(a => a.Status > 0).Where(a => a.FromWho.Value == UserId || a.ToWho.Value == UserId);
                    if (result.Any())
                    {
                        return result.ToList();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return new List<Message>();
        }
    }
}
