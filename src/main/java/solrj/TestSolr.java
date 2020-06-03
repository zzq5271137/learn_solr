package solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class TestSolr {

    // 部署的Solr服务器的地址
    private static final String SOLR_URL = "http://192.168.70.3:8080/solr";

    @Test
    public void testCreate() throws IOException, SolrServerException {
        HttpSolrServer httpSolrServer = new HttpSolrServer(SOLR_URL);
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", "001");
        doc.addField("title", "Switch");
        doc.addField("price", "2200");
        httpSolrServer.add(doc);
        httpSolrServer.commit();
    }

    @Test
    public void testUpdate() throws IOException, SolrServerException {
        HttpSolrServer httpSolrServer = new HttpSolrServer(SOLR_URL);
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id", "002");  // 如果id存在, 就更新; 如果id不存在, 就创建
        doc.setField("title", "Switch Lite");
        doc.setField("price", "1800");
        httpSolrServer.add(doc);
        httpSolrServer.commit();
    }

    @Test
    public void testDelete() throws IOException, SolrServerException {
        HttpSolrServer httpSolrServer = new HttpSolrServer(SOLR_URL);
        httpSolrServer.deleteById("001");
        httpSolrServer.commit();
    }

    @Test
    public void testDeleteAll() throws IOException, SolrServerException {
        HttpSolrServer httpSolrServer = new HttpSolrServer(SOLR_URL);
        httpSolrServer.deleteByQuery("*:*");
        httpSolrServer.commit();
    }

    @Test
    public void testGetAll() throws SolrServerException {
        HttpSolrServer httpSolrServer = new HttpSolrServer(SOLR_URL);
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);
        SolrDocumentList results = queryResponse.getResults();
        for (SolrDocument doc : results) {
            System.out.println("id: " + doc.get("id") + ", title: " + doc.get("title") + ", price: " + doc.get("price"));
        }
    }

}
