using System;
using System.Linq.Expressions;

namespace ProjectBase.Utils
{
    public class SortStruc<T>
    {
        public Expression<Func<T, Object>> OrderByExpression { get; set; }
        public OrderByDirectionEnum OrderByDirection { get; set; }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="sort">should be a comma delimetered string</param>
        /// <returns></returns>
        public static SortStruc<T>[] CreateFrom(string sort)
        {
            if (string.IsNullOrEmpty(sort)) return null;

            var sortStrucList = sort.Split(',');
            var a = new SortStruc<T>[sortStrucList.Length];
            var i = 0;
            foreach (var sortStrucString in sortStrucList)
            {
                var sortStrucPair = sortStrucString.Split(' ');
                var direction = sortStrucPair.Length == 1 ? "asc" : sortStrucPair[1].ToLower();
                a[i++] = new SortStruc<T>
                                    {
                                        OrderByExpression =
                                            System.Linq.Dynamic.DynamicExpression.ParseLambda<T, Object>(sortStrucPair[0]),
                                        OrderByDirection =
                                            direction == "asc"
                                                ? OrderByDirectionEnum.Asc
                                                : OrderByDirectionEnum.Desc
                                    };


            }
            return a;
        }
    }
}
