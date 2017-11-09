package com.kq.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.spark.status.api.v1.VersionInfo;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created with Intellij IDEA
 * Create User: keqiang.du
 * Date: 2017/9/14
 * Time: 下午2:09
 * Description:
 */
public class TxtFileIndexer {
    public static void main(String[] args) throws Exception {
//        String indexDir = "/Users/crazy/work/config/lucene/index";
//        String dataDir = "/Users/crazy/work/config/lucene/data";

        // Store the index in memory
        Directory directory = new RAMDirectory();
        // To store an index on disk
        // Directory directory1 = FSDirectory.open(indexDir)
        Version matchVersion = Version.LUCENE_6_6_0;
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory,config);
        Document doc = new Document();
        Document doc1 = new Document();
        String text = "Spark数据分析";
        String text1 = "Hadoop,Spark从入门到放弃";
        doc.add(new Field("Spark",text, TextField.TYPE_STORED));
        doc1.add(new Field("Spark",text1, TextField.TYPE_STORED));
        doc.add(new Field("Hadoop",text1, TextField.TYPE_STORED));
        indexWriter.deleteAll();
        indexWriter.addDocument(doc);
        indexWriter.addDocument(doc1);
        indexWriter.close();

        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        QueryParser parser = new QueryParser("Spark", analyzer);
        Query query = parser.parse(text);
        ScoreDoc[] hits = isearcher.search(query,1000).scoreDocs;
        System.out.println(hits.length);
//        assertEquals(2, hits.length);
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            System.out.println(hitDoc.get("Spark"));
//            assertEquals("This is the text to be indexed.", hitDoc.get("Spark"));
        }
        ireader.close();
        directory.close();
    }
}
