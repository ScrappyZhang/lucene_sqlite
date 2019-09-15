import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.PhraseQuery;
import org.apache.commons.lang3.StringUtils;

public class DemoSearch {
    public static void main(String[] args) throws ParseException, IOException{
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("./index")));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new SmartChineseAnalyzer();
        String keyword = "野辑此举鸽耙";
        QueryParser parser = new QueryParser("name",analyzer);
        if(!(keyword.isEmpty())){
            Query query = parser.parse("\""+keyword+"\"");
//            Query query = parser.parse(keyword);
//            Query query = new FuzzyQuery(new Term("name",keyword));
//            Query query = new WildcardQuery(new Term("name",keyword));
//            Query query = new TermQuery(new Term("name", keyword));
//            String key = StringUtils.join(keyword, ",");
//            String[] arr = keyword.split("");
//            Query query = new PhraseQuery(2, "name", arr);
            long startTime = System.currentTimeMillis(); //获取开始时间
            TopDocs results = searcher.search(query, 24);
            ScoreDoc[] hits = results.scoreDocs;
            int i=0;
            for(ScoreDoc hit: hits){
                Document document = searcher.doc(hit.doc);
                String idString = document.get("id");
                String nameString = document.get("name");
                System.out.println("id:"+idString+" name:"+nameString);
            }
            long endTime = System.currentTimeMillis(); //获取结束时间
            System.out.println(results.totalHits+"个记录");
            System.out.println("运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
        }
    }
}

