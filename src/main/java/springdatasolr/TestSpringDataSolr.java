package springdatasolr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springdatasolr.pojo.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-solr.xml"})
public class TestSpringDataSolr {

    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testAddItem() {
        Item item = new Item();
        item.setId(1L);
        item.setBrand("brand1");
        item.setCategory("category1");
        item.setPrice(new BigDecimal("1"));
        item.setTitle("title1");
        solrTemplate.saveBean(item);
        solrTemplate.commit();
    }

    @Test
    public void testAddItemList() {
        List<Item> itemList = new ArrayList<>();
        for (long i = 1; i < 100; i++) {
            Item item = new Item();
            item.setId(i);
            item.setBrand("brand" + i);
            item.setCategory("category" + i);
            item.setPrice(new BigDecimal("" + i));
            if (i % 2 == 0) {
                item.setTitle("挣命" + i);
            } else {
                item.setTitle("title" + i);
            }
            itemList.add(item);
        }
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }

    @Test
    public void testDeleteItem() {
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }

    @Test
    public void testDeleteItemAll() {
        SimpleQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    @Test
    public void testGetItem1() {
        /*
         * 分页查询
         */
        SimpleQuery query = new SimpleQuery("*:*");
        query.setOffset(10);  // 设置从哪条记录开始查
        query.setRows(5);  // 设置本次查几条数据/每页多少条数据
        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);
        System.out.println("总页数: " + items.getTotalPages());
        System.out.println("总记录数: " + items.getTotalElements());
        System.out.println("本次查询/每页多少条记录: " + items.getNumberOfElements());
        System.out.println("数据: ");
        List<Item> itemsContent = items.getContent();
        itemsContent.forEach(System.out::println);
    }

    @Test
    public void testGetItem2() {
        /*
         * 条件查询
         */
        SimpleQuery query = new SimpleQuery();
        Criteria titleCriteria = new Criteria("item_title").contains("挣命");
        Criteria priceCriteria = new Criteria("item_price").greaterThan(50);
        query.addCriteria(titleCriteria);
        query.addCriteria(priceCriteria);
        query.setOffset(10);
        query.setRows(5);
        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);
        System.out.println("总页数: " + items.getTotalPages());
        System.out.println("总记录数: " + items.getTotalElements());
        System.out.println("本次查询/每页多少条记录: " + items.getNumberOfElements());
        System.out.println("数据: ");
        List<Item> itemsContent = items.getContent();
        itemsContent.forEach(System.out::println);
    }

}
