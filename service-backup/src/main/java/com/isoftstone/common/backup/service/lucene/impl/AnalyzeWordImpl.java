package com.isoftstone.common.backup.service.lucene.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.isoftstone.common.backup.service.lucene.AnalyzeWord;

@Service
public class AnalyzeWordImpl implements AnalyzeWord {

	@Override
	public List<Map<String, Object>> analyzeWord(Map<String, Object> map) {
		List<Map<String, Object>> analyzeWord = new ArrayList<Map<String, Object>>();
		try {
			// 索引存放的位置...
			RAMDirectory directory = new RAMDirectory();
			analyzeWord = analyzeWord(directory, map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return analyzeWord;
	}

	public List<Map<String, Object>> analyzeWord(Directory directory, Map<String, Object> map) throws IOException {

		List<Map<String, String>> dataList = (List<Map<String, String>>) map.get("dataList");
		List<Map<String, Object>> tf = new ArrayList<Map<String, Object>>();
		if (dataList.size() > 0) {
			String tableName = dataList.get(0).get("table_name");
			// 创建索引
			createIndex(directory, dataList);
			// 读索引
			tf = getTF(directory, tableName);
		}
		directory.close();
		return tf;

	}

	public void createIndex(Directory directory, List<Map<String, String>> dataList) throws IOException {
		// 索引写入的配置
		Version matchVersion = Version.LATEST;// lucene当前匹配的版本
		IKAnalyzer ikAnalyzer = new IKAnalyzer();
		IndexWriterConfig conf = new IndexWriterConfig(matchVersion, ikAnalyzer);
		// 构建用于操作索引的类
		IndexWriter indexWriter = new IndexWriter(directory, conf);

		for (Map<String, String> data : dataList) {
			String str = data.get("content");

			// 通过IndexWriter来创建索引
			// 索引库里面的数据 要遵守一定的结构（索引结构，document）
			Document doc = new Document();
			// id域
			StringField id = new StringField("id", data.get("id"), Store.YES);
			// content 域
			FieldType ft = new FieldType();
			ft.setIndexed(true);// 存储
			ft.setStored(true);// 索引
			ft.setStoreTermVectors(true);
			ft.setTokenized(true);
			ft.setStoreTermVectorPositions(true);// 存储位置
			ft.setStoreTermVectorOffsets(true);// 存储偏移量
			ft.setStoreTermVectorPayloads(true);
			Field content = new Field("content", str, ft);

			doc.add(id);
			doc.add(content);
			// document里面也有很多字段
			indexWriter.addDocument(doc);
		}

		indexWriter.close();
		System.out.println("indexWriter.close");
	}

	public List<Map<String, Object>> getTF(Directory directory, String table_name) throws IOException {
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < indexReader.numDocs(); i++) {
			int docId = i;
			System.out.println("第" + (i + 1) + "篇文档：");
			String id = indexSearcher.doc(docId).get("id");
			Terms terms = indexReader.getTermVector(docId, "content");

			if (terms == null) {
				continue;
			}
			TermsEnum termsEnum = terms.iterator(null);
			BytesRef thisTerm = null;
			while ((thisTerm = termsEnum.next()) != null) {
				String termText = thisTerm.utf8ToString();

				DocsEnum docsEnum = termsEnum.docs(null, null);
				while ((docsEnum.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
					System.out.println("termText:" + termText + " TF:  " + docsEnum.freq());

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("table_name", table_name);
					map.put("table_id", id);
					map.put("name", termText);
					map.put("frq", docsEnum.freq());
					dataList.add(map);
				}

			}
		}

		indexReader.close();
		System.out.println("indexWriter.close");
		return dataList;
	}

}
