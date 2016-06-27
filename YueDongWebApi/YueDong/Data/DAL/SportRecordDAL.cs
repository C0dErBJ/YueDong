using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Data.Model;

namespace Data.DAL
{
    public class SportRecordDAL
    {
        public bool Insert(SportRecord model)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    context.SportRecords.Add(model);

                    return context.SaveChanges() > 0;
                }
            }
            catch (Exception e)
            {

            }
            return false;
        }

        public SportRecord GetSingleById(Guid Id)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.SportRecords.Where(a => a.Status > 0 && a.Id == Id);
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

        public List<SportRecord> GetList()
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.SportRecords.Where(a => a.Status > 0);
                    if (result.Any())
                    {
                        return result.ToList();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return new List<SportRecord>();
        }
        public List<SportRecord> GetListByUserId(Guid Id)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.SportRecords.Where(a => a.Status > 0 && a.UserId.Value == Id);
                    if (result.Any())
                    {
                        return result.ToList();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return new List<SportRecord>();
        }

        public bool Update(SportRecord model)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.SportRecords.FirstOrDefault(a => a.Id == model.Id);
                    if (result != null)
                    {
                        result.UpdateTime = model.UpdateTime;
                        result.SportLastTime = model.SportLastTime;
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
