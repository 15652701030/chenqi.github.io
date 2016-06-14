package com.chenqi.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;

/**
 * 查询索引库
 * Created with IntelliJ IDEA.
 *
 * @Author: 陈琪
 * @Date: 2016/6/14 22:41
 * To change this template use File | Settings | File Templates.
 */
public class IndexSearch {

    public static IndexSearcher getIndexSearcher() throws Exception{
        //获取索引库存放的路径
        String indexPath = "G:\\WorkSpace\\Temp\\Lucene";
        Directory directory = FSDirectory.open(new File(indexPath));
        //创建indexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建indexSearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        return indexSearcher;
    }

    public static void showResult(Query query,IndexSearcher indexSearcher) throws Exception{
        //查询索引库
        TopDocs topDocs = indexSearcher.search(query,10);
        //查询到结果的总数量
        System.out.println("查询到结果的总数量："+topDocs.totalHits);
        //遍历查询结果
        for (ScoreDoc scoreDoc : topDocs.scoreDocs){
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(document.get("filename"));
            System.out.println(document.get("content"));
            System.out.println(document.get("size"));
            System.out.println(document.get("path"));
        }

    }

    /**
     * 根据数值范围查询
     * @throws Exception
     */
    public void testNumricRangeQuery() throws Exception{
        //获得一个indexSearcher对象
        IndexSearcher indexSearcher = getIndexSearcher();

        //创建一个查询对象
        /**
         * 参数1：查询的域的名称
         * 参数2：最小值
         * 参数3：最大值
         * 参数4：是否包含最小值
         * 参数5：是否包含最大值
         */
        Query query = NumericRangeQuery.newLongRange("size",10l,300l,true,true);

        showResult(query,indexSearcher);

        //关闭indexSearcher
        indexSearcher.getIndexReader().close();
    }

    public void testBooleanQuery() throws Exception{
        IndexSearcher indexSearcher = getIndexSearcher();
        //创建一个查询对象
        BooleanQuery booleanClauses = new BooleanQuery();
        //创建查询条件
        Query query1 = new TermQuery(new Term("content","java"));
        Query query2 = new TermQuery(new Term("content","apache"));
        booleanClauses.add(query1, BooleanClause.Occur.MUST);
        booleanClauses.add(query2, BooleanClause.Occur.MUST);

        //执行查询
        showResult(booleanClauses,indexSearcher);
        //关闭indexSearcher
        indexSearcher.getIndexReader().close();
    }

    public void testMatchAddDocsQuery() throws Exception{
        IndexSearcher indexSearcher = getIndexSearcher();
        //创建一个查询对象
        Query query = new MatchAllDocsQuery();
        showResult(query,indexSearcher);
        //关闭indexSearcher
        indexSearcher.getIndexReader().close();
    }

}
