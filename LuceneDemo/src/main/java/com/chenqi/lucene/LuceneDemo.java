package com.chenqi.lucene;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Lucene的入门程序
 * @author Administrator
 *
 */
public class LuceneDemo {

	// 创建索引库
	public static void createIndex() throws Exception{
		IndexWriter indexWriter = IndexManager.getIndexWriter();
		//遍历原始文档文件夹下的所有文件 将文件内容读取到程序中
		File path = new File("G:\\WorkSpace\\Temp\\Lucene\\searchsource");
		for (File file : path.listFiles()) {
			// 创建document对象
			Document document = new Document();
			String fileName = file.getName();
			//获取文件内容
			String fileContent = FileUtils.readFileToString(file);
			//文件路径
			String filePath = file.getPath();
			//文件大小
			long fileSize  = FileUtils.sizeOf(file);
			//向文档中添加域
			Field fileNameField = new TextField("filename", fileName, Store.YES);
			Field fileContentField = new TextField("content", fileContent, Store.YES);
			Field filePathField = new StoredField("path", filePath);
			Field fileSizeField = new LongField("size", fileSize, Store.YES);
			document.add(fileNameField);
			document.add(fileContentField);
			document.add(filePathField);
			document.add(fileSizeField);
			
			//将document写入索引库
			indexWriter.addDocument(document);
		}
		indexWriter.close();
	}

    //查询索引
    public static void searchIndex() throws Exception{
        //获取索引库存放的路径
        String indexPath = "G:\\WorkSpace\\Temp\\Lucene";
        Directory directory = FSDirectory.open(new File(indexPath));
        //创建indexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建indexSearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //创建查询
        Query query = new TermQuery(new Term("content","apache"));
        //执行查询 第一个参数是查询对象 第二个参数是返回结果的最大值
        TopDocs topDocs = indexSearcher.search(query,10);
        System.out.println("查询结果的总数量：" + topDocs.totalHits);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            //scoreDoc.doc文档的唯一id可以根据这个id去document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            //取文档的属性内容
            System.out.println(document.get("filename"));
            //System.out.println(document.get("content"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
        }
        indexReader.close();
    }

    /**
     * 测试分析器的分词效率
     * @throws Exception
     */
    public static void testTokenStream() throws Exception{
        //创建分词分析器的对象
        //标准分析器
        //Analyzer analyzer = new StandardAnalyzer();
        //CJKAnalyzer
        //Analyzer analyzer = new CJKAnalyzer();
        Analyzer analyzer = new SmartChineseAnalyzer();
        //获取tokenStream
        //TokenStream tokenStream = analyzer.tokenStream("test", "Serving Web Content with Spring MVC");
        TokenStream tokenStream = analyzer.tokenStream("test", "我是一只小小鸟");
        //调整指针至最顶端
        tokenStream.reset();
        //获取当前词的一个引用
        CharTermAttribute termAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        //获取当前词的偏移量引用
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        //遍历tokenStream
        while (tokenStream.incrementToken()){
            //获取当前词的开始位置
            System.out.println(offsetAttribute.startOffset());
            //获取当前词内容
            System.out.println(termAttribute);
            //获取当前词的结束位置
            System.out.println(offsetAttribute.endOffset());
        }
        tokenStream.close();
        analyzer.close();
    }


	public static void main(String[] args) {
		try {
			//createIndex();
            //searchIndex();
            testTokenStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
