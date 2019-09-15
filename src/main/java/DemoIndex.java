import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import javax.print.Doc;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.io.UnsupportedEncodingException;
import java.util.Random;


public class DemoIndex {
//    protected static String[] ids = {"1", "2"};
//    protected static String[] content = {"lv qin jie", "lv jian jun"};
//    private static  Directory dir;

//    public static String getRandomChar() {
//        char text = (char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1)));
//        return (String) Character.toString(text) ;
//    }
    public static String getRandomChar() {
        String str = "";
        int hightPos; //
        int lowPos;

        Random random = new Random();

        hightPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("错误");
        }

        return str;
    }


    public static void main(String[] args) throws Exception {
        Connection connection = null;
        String pathFile = "./index";
        String sql;
        String text;
        IndexWriter writer = new IndexWriter(FSDirectory.open(Paths.get(pathFile)), new IndexWriterConfig(new SmartChineseAnalyzer()));
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection ("jdbc:sqlite:access.db");
            Statement stat = conn.createStatement();
            stat.setQueryTimeout(30);
//            stat.executeUpdate ( "CREATE TABLE person (id INTEGER NOT NULL, name VARCHAR(255))" ) ;
//            long startTime = System.currentTimeMillis(); //获取开始时间
//            stat.executeUpdate("begin transaction");
//            for (int i = 0; i < 3000000; i++){
//                text = getRandomChar() + getRandomChar() + getRandomChar() + getRandomChar() + getRandomChar() + getRandomChar();
//                sql = "insert into person values ( '" + String.valueOf(i) + "','" + text + "')" ;
//                stat.executeUpdate(sql);
//            }
//            stat.executeUpdate("commit transaction");
//            long endTime = System.currentTimeMillis(); //获取结束时间
//            System.out.println("插入数据运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
//            for (int i = 0; i < ids.length; i++){
////                text = getRandomChar() + getRandomChar() + getRandomChar() + getRandomChar();
////                sql = "insert into vt values('" + text + "');" ;
//                System.out.println(ids[i] + content[i]);
//                sql = "insert into person values ( '" + ids[i] + "','" + content[i] + "')" ;
//                stat.executeUpdate(sql);
//            }
            ResultSet rs = stat.executeQuery("select * from person");
            long startTime = System.currentTimeMillis(); //获取开始时间
            while (rs.next()){
                Document doc = new Document();
                doc.add(new TextField("id", rs.getString("id"), Field.Store.YES));
                doc.add(new TextField("name", rs.getString("name"), Field.Store.YES));
                writer.addDocument(doc);
            }
            long endTime = System.currentTimeMillis(); //获取结束时间
            System.out.println("建立索引运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
        } catch (SQLException e){
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection!= null) connection.close();
                if (writer != null) writer.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

}
