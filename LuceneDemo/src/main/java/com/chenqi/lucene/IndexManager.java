package com.chenqi.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;

/**
 * 索引库的维护
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈琪
 * @Date: 2016/6/14 22:17
 * To change this template use File | Settings | File Templates.
 */
public class IndexManager {

    public static IndexWriter getIndexWriter() throws Exception{
        // 指定索引库存放的位置
        String indexPath = "G:\\WorkSpace\\Temp\\Lucene";
        //将索引库存放到内存中
        //Directory directory = new RAMDirectory();
        // 存放到文件系统中
        Directory directory = FSDirectory.open(new File(indexPath));
        // 创建分析器
        Analyzer analyzer = new StandardAnalyzer();
        // 创建indexWriterConfig对象
        //第一个参数：Lucene使用的版本
        //第二个参数：分析器对象
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        //创建indexWriter对象
        IndexWriter indexWriter = new IndexWriter(directory,config);
        return indexWriter;
    }

    /**
     * 向索引库中添加文档
     * @throws Exception
     */
    public static void addDocument() throws Exception{
        //获取indexWriter对象
        IndexWriter indexWriter = getIndexWriter();
        //创建文档对象
        Document document = new Document();
        //添加域
        Field fileName = new TextField("filename","新添加的文档", Field.Store.YES);
        Field content = new TextField("content","哈哈换货", Field.Store.NO);
        //添加到document
        document.add(fileName);
        document.add(content);
        //添加到索引库
        indexWriter.addDocument(document);
        indexWriter.close();
    }

    /**
     * 删除全部文档
     * @throws Exception
     */
    public void deleteAllDocument() throws Exception{
        //获得indexWriter对象
        IndexWriter indexWriter = getIndexWriter();
        indexWriter.deleteAll();
        indexWriter.close();
    }

    /**
     * 通过查询条件删除文档
     * @throws Exception
     */
    public void deleteDocumentByQuery() throws Exception{
        //获得indexWriter对象
        IndexWriter indexWriter = getIndexWriter();
        //创建一个查询
        Query query = new TermQuery(new Term("content","java"));
        indexWriter.deleteDocuments(query);
        indexWriter.close();
    }

    public void updateDocument() throws Exception{
        //获得indexWriter对象
        IndexWriter indexWriter = getIndexWriter();
        //创建一个新的文档
        Document document = new Document();
        //添加域
        Field fileName = new TextField("filename","新添加的文档", Field.Store.YES);
        Field content = new TextField("content","哈哈换货", Field.Store.NO);
        //添加到document
        document.add(fileName);
        document.add(content);

        indexWriter.updateDocument(new Term("content","apache"),document);
        indexWriter.close();
    }
}
