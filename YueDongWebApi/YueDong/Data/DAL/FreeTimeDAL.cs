using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Data.Model;

namespace Data.DAL
{
    public class FreeTimeDAL
    {
        public bool Insert(FreeTime model)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    context.FreeTimes.Add(model);
                    return context.SaveChanges() > 0;
                }
            }
            catch (Exception e)
            {

            }
            return false;
        }

        public FreeTime GetSingleById(Guid Id)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.FreeTimes.Where(a => a.Status > 0 && a.Id == Id);
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

        public List<FreeTime> GetList()
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.FreeTimes.Where(a => a.Status > 0);
                    if (result.Any())
                    {
                        return result.ToList();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return new List<FreeTime>();
        }
        public List<FreeTime> GetListByUserId(Guid UserId)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.FreeTimes.Where(a => a.Status > 0 && a.UserId == UserId);
                    if (result.Any())
                    {
                        return result.ToList();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return new List<FreeTime>();
        }

        public bool Update(FreeTime model)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.FreeTimes.FirstOrDefault(a => a.Id == model.Id);
                    if (result != null)
                    {
                        result.StartTime = model.StartTime;
                        result.EndTime = model.EndTime;
                        result.Week = model.Week;
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

        public bool DeleteByUserIdAndWeekTime(FreeTime time)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.FreeTimes.Where(a => a.Status > 0 && a.UserId == time.UserId && time.Week == a.Week.Value && a.StartTime.Value.ToString("HH:mm") == time.StartTime.Value.ToString("HH:mm") && a.EndTime.Value.ToString("HH:mm") == time.EndTime.Value.ToString("HH:mm"));
                    if (result.Any())
                    {
                        result.FirstOrDefault().Status = 0;
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
