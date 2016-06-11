package com.chenqi.lucene;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
        Query query = new TermQuery(new Term("conten","apache"));
        //执行查询 第一个参数是查询对象 第二个参数是返回结果的最大值
        TopDocs topDocs = indexSearcher.search(query,10);
        System.out.println("查询结果的总数量：" + topDocs.totalHits);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            //scoreDoc.doc文档的唯一id可以根据这个id去document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            //取文档的属性内容
            System.out.println(document.get("filename"));
            //System.out.println(document.get("conten"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
        }
        indexReader.close();
    }
	public static void main(String[] args) {
		try {
			//createIndex();
            searchIndex();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
