import java.sql.*;
import org.sqlite.JDBC;

public class Dbt {

    public static String getRandomChar() {
        char text = (char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1)));
        return (String) Character.toString(text) ;
    }

    public static void main(String[] args) {
        int i;
        String sql;
        String text;
        try {
            //连接SQLite的JDBC
            Class.forName( "org.sqlite.JDBC") ;
            //建立一个数据库名zieckey.db的连接，如果不存在就在当前目录下创建之
            Connection conn = DriverManager.getConnection ("jdbc:sqlite:ziecke.db");
            Statement stat = conn.createStatement();
            stat.executeUpdate ( "CREATE TABLE vt NAME TEXT NOT NULL" ) ;
//            stat.executeUpdate ( "CREATE VIRTUAL TABLE vt USING fts4(n TEXT);" ) ; //创建一个表，两列
//            stat.executeUpdate ( "insert into vt values('kiwi bird', '123 456 6789', 'dog');" ) ; //插入数据
//            stat.executeUpdate ( "insert into vt values('张朝扬', '123 456 6789', 'dog');" ) ;
//            stat.executeUpdate ( "insert into vt values('张飞你', '123 456 6789', 'dog');" ) ;
//            stat.executeUpdate ( "insert into vt values('北京欢迎你', '123 456 6789', 'dog');" ) ;
//            stat.executeUpdate("begin transaction");
            for (i = 0; i < 3000000; i++){
                text = getRandomChar() + getRandomChar() + getRandomChar() + getRandomChar();
                sql = "insert into vt values('" + text + "');" ;
                stat.executeUpdate(sql);
            }
            stat.executeUpdate("commit transaction");
            long startTime = System.currentTimeMillis(); //获取开始时间
            ResultSet rs = stat.executeQuery ( "select * from vt where  name match '张';" ) ; //查询数据
            while (rs.next()) { //将查询到的数据打印出来
                System.out.print(" name = " + rs.getString ( "name" )) ; //列属性一
                rs.close();
                conn.close(); //结束数据库的连接
                break;
            }
            long endTime = System.currentTimeMillis(); //获取结束时间
            System.out.println("程序运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
