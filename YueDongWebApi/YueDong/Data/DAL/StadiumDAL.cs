using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Data.Model;

namespace Data.DAL
{
    public class StadiumDAL
    {
        public bool Insert(Stadium model)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    context.Stadia.Add(model);

                    return context.SaveChanges() > 0;
                }
            }
            catch (Exception e)
            {

            }
            return false;
        }

        public Stadium GetSingleById(Guid Id)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.Stadia.Where(a => a.Status > 0 && a.Id == Id);
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

        public List<Stadium> GetList()
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.Stadia.Where(a => a.Status > 0);
                    if (result.Any())
                    {
                        return result.ToList();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return new List<Stadium>();
        }
        public List<Stadium> GetListByKeyWord(string keyword)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.Stadia.Where(a => a.Status > 0).Where(a=>a.Name.Contains(keyword)
                    ||a.Address.Contains(keyword)
                    );
                    if (result.Any())
                    {
                        return result.ToList();
                    }
                }
            }
            catch (Exception e)
            {

            }
            return new List<Stadium>();
        }

        public bool Update(Stadium model)
        {
            try
            {
                using (var context = new YueDongEntities())
                {
                    var result = context.Stadia.FirstOrDefault(a => a.Id == model.Id);
                    if (result != null)
                    {
                        result.UpdateTime = model.UpdateTime;
                        result.Address = model.Address;
                        result.Name = model.Name;
                        result.OpenEndTime = model.OpenEndTime;
                        result.OpenStartTime = model.OpenStartTime;
                        result.Phone = model.Phone;
                        result.Price = model.Price;
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
