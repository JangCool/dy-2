package kr.co.pionnet.dy;
public class DataSourceContextHolder {

   private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

   public static void setDataSourceType(String dataSourceType) {
       if(dataSourceType == null){
           throw new NullPointerException();
       }
      contextHolder.set(dataSourceType);
   }

   public static String getDataSourceType() {
      return (String) contextHolder.get();
   }

   public static void clear() {
      contextHolder.remove();
   }
}